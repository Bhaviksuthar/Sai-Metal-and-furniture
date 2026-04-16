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
    override fun getServices(): List<ServiceCategory> = listOf(
        ServiceCategory("gates", "Designer Gates", "Main gates, sliding gates, compound entries", "Custom fabricated gates with powder coating, laser-cut accents, and site-fit installation.", "From Rs 28,000", "gate"),
        ServiceCategory("railings", "Railings & Stairs", "Balcony, staircase, terrace safety solutions", "Modern MS, SS, and hybrid railing systems with polished finishing and strong joinery.", "From Rs 12,000", "stairs"),
        ServiceCategory("bedroom", "Bedroom Furniture", "Beds, wardrobes, side tables, dressing units", "Storage-focused bedroom sets designed around room size, materials, and usage.", "From Rs 18,000", "bed"),
        ServiceCategory("office", "Office & Commercial", "Counters, cabins, seating, storage", "Durable furniture and fabrication work for shops, clinics, and offices.", "From Rs 22,000", "office")
    )

    override fun getGallery(): List<GalleryItem> = listOf(
        GalleryItem("1", "Laser Cut Main Gate", "Metal Works", "Surat", "Powder-coated MS gate with geometric CNC cut pattern and side wicket door.", "Rs 55,000 - Rs 72,000", "8-10 days", listOf("Premium finish", "Outdoor", "Custom design"), 0xFF7A4B2A, ""),
        GalleryItem("2", "Modern King Bed Set", "Furniture", "Navsari", "Metal and laminate bed with hydraulic storage and matching side units.", "Rs 38,000 - Rs 49,000", "6-8 days", listOf("Bedroom", "Storage", "Laminate"), 0xFF355C4A, ""),
        GalleryItem("3", "Stair Railing Installation", "Metal Works", "Bardoli", "Matte black stair railing with wood-top handrest for duplex interiors.", "Rs 24,000 - Rs 34,000", "5-7 days", listOf("Indoor", "Safety", "Elegant"), 0xFF45546E, ""),
        GalleryItem("4", "Showroom Display Counter", "Commercial", "Vapi", "Fabricated counter and wall display furniture with integrated storage.", "Rs 60,000 - Rs 85,000", "10-14 days", listOf("Retail", "Storage", "Custom size"), 0xFF735A2F, "")
    )

    override fun getInquiries(): List<Inquiry> = emptyList()

    override fun getBillingRecords(): List<BillingRecord> = emptyList()

    override fun getDashboardMetrics(): List<DashboardMetric> = listOf(
        DashboardMetric("Active Works", "12", "4 deliveries due this week"),
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
    val metrics: List<DashboardMetric>
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
            metrics = fallback.getDashboardMetrics()
        )

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

        return DashboardBundle(
            services = fallback.getServices(),
            gallery = gallery,
            inquiries = inquiries,
            billing = billing,
            metrics = listOf(
                DashboardMetric("Active Works", gallery.size.toString(), "Live count from gallery records"),
                DashboardMetric("New Leads", inquiries.count { it.status.equals("New", ignoreCase = true) }.toString(), "Count of open fresh inquiries"),
                DashboardMetric("Pending Amount", billing.firstOrNull()?.dueAmount ?: "Rs 0", "Use Firestore billing entries for real totals"),
                DashboardMetric("Client Rating", "4.9", "Replace with your own feedback collection later")
            )
        )
    }
}
