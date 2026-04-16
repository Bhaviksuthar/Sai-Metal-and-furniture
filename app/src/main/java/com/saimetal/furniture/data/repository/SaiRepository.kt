package com.saimetal.furniture.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.saimetal.furniture.BuildConfig
import com.saimetal.furniture.data.model.BillingRecord
import com.saimetal.furniture.data.model.BillingDraft
import com.saimetal.furniture.data.model.DashboardMetric
import com.saimetal.furniture.data.model.GalleryItem
import com.saimetal.furniture.data.model.Inquiry
import com.saimetal.furniture.data.model.QuoteDraft
import com.saimetal.furniture.data.model.ServiceDraft
import com.saimetal.furniture.data.model.ServiceCategory
import com.saimetal.furniture.data.model.WorkDraft
import kotlinx.coroutines.tasks.await

interface SaiRepository {
    fun getServices(): List<ServiceCategory>
    fun getGallery(): List<GalleryItem>
    fun getInquiries(): List<Inquiry>
    fun getBillingRecords(): List<BillingRecord>
    fun getDashboardMetrics(): List<DashboardMetric>
}

class LocalSaiRepository : SaiRepository {
    override fun getServices(): List<ServiceCategory> = emptyList()

    override fun getGallery(): List<GalleryItem> = emptyList()

    override fun getInquiries(): List<Inquiry> = emptyList()

    override fun getBillingRecords(): List<BillingRecord> = emptyList()

    override fun getDashboardMetrics(): List<DashboardMetric> = listOf(
        DashboardMetric("Active Works", "0", "Add gallery works to see them here"),
        DashboardMetric("New Leads", "0", "Customer inquiries will appear here"),
        DashboardMetric("Pending Amount", "Rs 0", "Billing entries will update this total"),
        DashboardMetric("Client Rating", "4.9", "Based on recent project feedback")
    )
}

data class DashboardBundle(
    val services: List<ServiceCategory>,
    val gallery: List<GalleryItem>,
    val inquiries: List<Inquiry>,
    val billing: List<BillingRecord>,
    val metrics: List<DashboardMetric>,
    val ownerPhone: String
)

data class FirebaseInquiry(
    val id: String = "",
    val customerName: String = "",
    val phone: String = "",
    val workType: String = "",
    val message: String = "",
    val status: String = "",
    val budget: String = ""
)

data class FirebaseGalleryItem(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val location: String = "",
    val description: String = "",
    val priceRange: String = "",
    val completionDays: String = "",
    val tags: List<String> = emptyList(),
    val accent: Long = 0xFF7A4B2A,
    val imageUrl: String = ""
)

data class FirebaseBillingRecord(
    val id: String = "",
    val clientName: String = "",
    val projectTitle: String = "",
    val totalAmount: String = "",
    val advancePaid: String = "",
    val dueAmount: String = "",
    val dueDate: String = "",
    val paymentStatus: String = ""
)

data class FirebaseServiceItem(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val startingPrice: String = "",
    val icon: String = "office",
    val imageUrl: String = ""
)

data class FirebaseBusinessSettings(
    val ownerPhone: String = "9913310429"
)

class FirebaseSaiRepository : SaiRepository {
    private val firestore: FirebaseFirestore? = if (BuildConfig.FIREBASE_ENABLED) FirebaseFirestore.getInstance() else null
    private val auth: FirebaseAuth? = if (BuildConfig.FIREBASE_ENABLED) FirebaseAuth.getInstance() else null
    private val storage: FirebaseStorage? = if (BuildConfig.FIREBASE_ENABLED) FirebaseStorage.getInstance() else null
    private val fallback = LocalSaiRepository()

    override fun getServices(): List<ServiceCategory> = fallback.getServices()
    override fun getGallery(): List<GalleryItem> = fallback.getGallery()
    override fun getInquiries(): List<Inquiry> = fallback.getInquiries()
    override fun getBillingRecords(): List<BillingRecord> = fallback.getBillingRecords()
    override fun getDashboardMetrics(): List<DashboardMetric> = fallback.getDashboardMetrics()

    fun isFirebaseConfigured(): Boolean = firestore != null && auth != null && storage != null

    fun isAdminSignedIn(): Boolean = auth?.currentUser != null

    fun signOutAdmin() {
        auth?.signOut()
    }

    suspend fun signInAdmin(email: String, password: String): Boolean {
        val firebaseAuth = auth ?: return false
        return runCatching { firebaseAuth.signInWithEmailAndPassword(email, password).await() }.isSuccess
    }

    suspend fun submitInquiry(draft: QuoteDraft) {
        val db = firestore ?: return
        val document = if (draft.customerName.isBlank()) return else db.collection("inquiries").document()
        val payload = FirebaseInquiry(
            id = document.id,
            customerName = draft.customerName,
            phone = draft.phone,
            workType = draft.workType.ifBlank { "Custom Work" },
            message = draft.message.ifBlank { "Interested in discussing requirements." },
            status = "New",
            budget = draft.budget.ifBlank { "Discuss on call" }
        )
        document.set(payload).await()
    }

    suspend fun addGalleryWork(draft: WorkDraft) {
        val db = firestore ?: return
        val document = if (draft.title.isBlank()) return else db.collection("gallery_items").document()
        val payload = FirebaseGalleryItem(
            id = document.id,
            title = draft.title,
            category = draft.category,
            location = draft.location.ifBlank { "Sai Workshop" },
            description = draft.description.ifBlank { "Custom-built project added by admin." },
            priceRange = draft.priceRange.ifBlank { "Price on request" },
            completionDays = draft.duration.ifBlank { "As per project scope" },
            tags = listOf("Fresh upload", "Admin added", draft.category),
            accent = 0xFF6B4F3A,
            imageUrl = draft.imageUrl
        )
        document.set(payload).await()
    }

    suspend fun updateGalleryWork(draft: WorkDraft) {
        val db = firestore ?: return
        if (draft.id.isBlank() || draft.title.isBlank()) return
        val payload = FirebaseGalleryItem(
            id = draft.id,
            title = draft.title,
            category = draft.category,
            location = draft.location.ifBlank { "Sai Workshop" },
            description = draft.description.ifBlank { "Custom-built project updated by admin." },
            priceRange = draft.priceRange.ifBlank { "Price on request" },
            completionDays = draft.duration.ifBlank { "As per project scope" },
            tags = listOf("Updated", "Admin managed", draft.category),
            accent = 0xFF6B4F3A,
            imageUrl = draft.imageUrl
        )
        db.collection("gallery_items").document(draft.id).set(payload).await()
    }

    suspend fun deleteGalleryWork(id: String) {
        val db = firestore ?: return
        if (id.isBlank()) return
        db.collection("gallery_items").document(id).delete().await()
    }

    suspend fun addBillingRecord(draft: BillingDraft) {
        val db = firestore ?: return
        val document = if (draft.clientName.isBlank()) return else db.collection("billings").document()
        val payload = FirebaseBillingRecord(
            id = document.id,
            clientName = draft.clientName,
            projectTitle = draft.projectTitle,
            totalAmount = draft.totalAmount,
            advancePaid = draft.advancePaid,
            dueAmount = draft.dueAmount,
            dueDate = draft.dueDate,
            paymentStatus = draft.paymentStatus.ifBlank { "Pending" }
        )
        document.set(payload).await()
    }

    suspend fun updateBillingRecord(draft: BillingDraft) {
        val db = firestore ?: return
        if (draft.id.isBlank() || draft.clientName.isBlank()) return
        val payload = FirebaseBillingRecord(
            id = draft.id,
            clientName = draft.clientName,
            projectTitle = draft.projectTitle,
            totalAmount = draft.totalAmount,
            advancePaid = draft.advancePaid,
            dueAmount = draft.dueAmount,
            dueDate = draft.dueDate,
            paymentStatus = draft.paymentStatus.ifBlank { "Pending" }
        )
        db.collection("billings").document(draft.id).set(payload).await()
    }

    suspend fun deleteBillingRecord(id: String) {
        val db = firestore ?: return
        if (id.isBlank()) return
        db.collection("billings").document(id).delete().await()
    }

    suspend fun addService(draft: ServiceDraft) {
        val db = firestore ?: return
        val document = if (draft.title.isBlank()) return else db.collection("services").document()
        val payload = FirebaseServiceItem(
            id = document.id,
            title = draft.title,
            subtitle = draft.subtitle,
            description = draft.description,
            startingPrice = draft.startingPrice,
            icon = draft.icon,
            imageUrl = draft.imageUrl
        )
        document.set(payload).await()
    }

    suspend fun updateService(draft: ServiceDraft) {
        val db = firestore ?: return
        if (draft.id.isBlank() || draft.title.isBlank()) return
        val payload = FirebaseServiceItem(
            id = draft.id,
            title = draft.title,
            subtitle = draft.subtitle,
            description = draft.description,
            startingPrice = draft.startingPrice,
            icon = draft.icon,
            imageUrl = draft.imageUrl
        )
        db.collection("services").document(draft.id).set(payload).await()
    }

    suspend fun deleteService(id: String) {
        val db = firestore ?: return
        if (id.isBlank()) return
        db.collection("services").document(id).delete().await()
    }

    suspend fun saveOwnerPhone(number: String) {
        val db = firestore ?: return
        db.collection("settings").document("business_contact")
            .set(FirebaseBusinessSettings(ownerPhone = number))
            .await()
    }

    suspend fun uploadGalleryImage(uri: Uri): String {
        val firebaseStorage = storage ?: return ""
        val imageRef = firebaseStorage.reference
            .child("gallery")
            .child("${System.currentTimeMillis()}-${uri.lastPathSegment ?: "work-image"}")

        imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }

    suspend fun loadDashboardBundle(): DashboardBundle {
        val db = firestore ?: return DashboardBundle(
            services = fallback.getServices(),
            gallery = fallback.getGallery(),
            inquiries = fallback.getInquiries(),
            billing = fallback.getBillingRecords(),
            metrics = fallback.getDashboardMetrics(),
            ownerPhone = "9913310429"
        )

        val services = db.collection("services")
            .orderBy("title", Query.Direction.ASCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { snapshot ->
                snapshot.toObject<FirebaseServiceItem>()?.let {
                    ServiceCategory(it.id, it.title, it.subtitle, it.description, it.startingPrice, it.icon, it.imageUrl)
                }
            }

        val gallery = db.collection("gallery_items")
            .orderBy("title", Query.Direction.ASCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { snapshot ->
                snapshot.toObject<FirebaseGalleryItem>()?.let {
                    GalleryItem(it.id, it.title, it.category, it.location, it.description, it.priceRange, it.completionDays, it.tags, it.accent, it.imageUrl)
                }
            }
            .ifEmpty { fallback.getGallery() }

        val inquiries = db.collection("inquiries")
            .orderBy("customerName", Query.Direction.ASCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { snapshot ->
                snapshot.toObject<FirebaseInquiry>()?.let {
                    Inquiry(it.id, it.customerName, it.phone, it.workType, it.message, it.status, it.budget)
                }
            }
            .ifEmpty { fallback.getInquiries() }

        val billing = db.collection("billings")
            .orderBy("clientName", Query.Direction.ASCENDING)
            .get()
            .await()
            .documents
            .mapNotNull { snapshot ->
                snapshot.toObject<FirebaseBillingRecord>()?.let {
                    BillingRecord(it.id, it.clientName, it.projectTitle, it.totalAmount, it.advancePaid, it.dueAmount, it.dueDate, it.paymentStatus)
                }
            }
            .ifEmpty { fallback.getBillingRecords() }

        val ownerPhone = db.collection("settings")
            .document("business_contact")
            .get()
            .await()
            .toObject<FirebaseBusinessSettings>()
            ?.ownerPhone
            ?.ifBlank { "9913310429" }
            ?: "9913310429"

        return DashboardBundle(
            services = services,
            gallery = gallery,
            inquiries = inquiries,
            billing = billing,
            metrics = listOf(
                DashboardMetric("Active Works", gallery.size.toString(), "Live count from gallery records"),
                DashboardMetric("New Leads", inquiries.count { it.status.equals("New", ignoreCase = true) }.toString(), "Count of open fresh inquiries"),
                DashboardMetric("Pending Amount", billing.firstOrNull()?.dueAmount ?: "Rs 0", "Use Firestore billing entries for real totals"),
                DashboardMetric("Client Rating", "4.9", "Replace with your own feedback collection later")
            ),
            ownerPhone = ownerPhone
        )
    }
}
