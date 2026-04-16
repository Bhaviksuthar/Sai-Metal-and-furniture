package com.saimetal.furniture.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saimetal.furniture.BuildConfig
import com.saimetal.furniture.data.model.GalleryItem
import com.saimetal.furniture.data.model.Inquiry
import com.saimetal.furniture.data.model.QuoteDraft
import com.saimetal.furniture.data.model.WorkDraft
import com.saimetal.furniture.data.repository.FirebaseSaiRepository
import com.saimetal.furniture.data.repository.LocalSaiRepository
import com.saimetal.furniture.data.repository.SaiRepository
import kotlinx.coroutines.launch

class SaiMetalViewModel(
    private val repository: SaiRepository = if (BuildConfig.FIREBASE_ENABLED) FirebaseSaiRepository() else LocalSaiRepository()
) : ViewModel() {
    private val firebaseRepository = (repository as? FirebaseSaiRepository)

    val services = mutableStateListOf<com.saimetal.furniture.data.model.ServiceCategory>().apply { addAll(repository.getServices()) }
    val metrics = mutableStateListOf<com.saimetal.furniture.data.model.DashboardMetric>().apply { addAll(repository.getDashboardMetrics()) }
    val gallery = mutableStateListOf<GalleryItem>().apply { addAll(repository.getGallery()) }
    val inquiries = mutableStateListOf<Inquiry>().apply { addAll(repository.getInquiries()) }
    val billing = mutableStateListOf<com.saimetal.furniture.data.model.BillingRecord>().apply { addAll(repository.getBillingRecords()) }

    var quoteDraft by mutableStateOf(QuoteDraft())
        private set

    var workDraft by mutableStateOf(WorkDraft())
        private set

    var adminLoggedIn by mutableStateOf(false)
        private set

    var adminLoginMessage by mutableStateOf("Sign in with your Firebase Authentication admin email and password.")
        private set

    var adminLoginInProgress by mutableStateOf(false)
        private set

    var adminWorkMessage by mutableStateOf("Manage gallery works from here.")
        private set

    var firebaseStatus by mutableStateOf(
        if (firebaseRepository?.isFirebaseConfigured() == true) "Firebase ready" else "Demo data mode"
    )
        private set

    init {
        refreshFromFirebase()
    }

    fun updateQuoteDraft(transform: QuoteDraft.() -> QuoteDraft) {
        quoteDraft = quoteDraft.transform()
    }

    fun submitInquiry() {
        if (quoteDraft.customerName.isBlank() || quoteDraft.phone.isBlank()) return
        val currentDraft = quoteDraft
        inquiries.add(
            0,
            Inquiry(
                id = "I-${100 + inquiries.size + 1}",
                customerName = currentDraft.customerName,
                phone = currentDraft.phone,
                workType = currentDraft.workType.ifBlank { "Custom Work" },
                message = currentDraft.message.ifBlank { "Interested in discussing requirements." },
                status = "New",
                budget = currentDraft.budget.ifBlank { "Discuss on call" }
            )
        )
        quoteDraft = QuoteDraft()
        viewModelScope.launch {
            runCatching { firebaseRepository?.submitInquiry(currentDraft) }
        }
    }

    fun updateWorkDraft(transform: WorkDraft.() -> WorkDraft) {
        workDraft = workDraft.transform()
    }

    fun addGalleryWork() {
        if (workDraft.title.isBlank() || workDraft.category.isBlank()) return
        val currentDraft = workDraft
        if (currentDraft.id.isBlank()) {
            gallery.add(
                0,
                GalleryItem(
                    id = "W-${gallery.size + 1}",
                    title = currentDraft.title,
                    category = currentDraft.category,
                    location = currentDraft.location.ifBlank { "Sai Workshop" },
                    description = currentDraft.description.ifBlank { "Custom-built project added by admin." },
                    priceRange = currentDraft.priceRange.ifBlank { "Price on request" },
                    completionDays = currentDraft.duration.ifBlank { "As per project scope" },
                    tags = listOf("Fresh upload", "Admin added", currentDraft.category),
                    accent = 0xFF6B4F3A,
                    imageUrl = currentDraft.imageUrl
                )
            )
            adminWorkMessage = "New work added successfully."
        } else {
            val index = gallery.indexOfFirst { it.id == currentDraft.id }
            if (index >= 0) {
                gallery[index] = GalleryItem(
                    id = currentDraft.id,
                    title = currentDraft.title,
                    category = currentDraft.category,
                    location = currentDraft.location.ifBlank { "Sai Workshop" },
                    description = currentDraft.description.ifBlank { "Custom-built project updated by admin." },
                    priceRange = currentDraft.priceRange.ifBlank { "Price on request" },
                    completionDays = currentDraft.duration.ifBlank { "As per project scope" },
                    tags = listOf("Updated", "Admin managed", currentDraft.category),
                    accent = gallery[index].accent,
                    imageUrl = currentDraft.imageUrl
                )
            }
            adminWorkMessage = "Work updated successfully."
        }
        workDraft = WorkDraft()
        viewModelScope.launch {
            runCatching {
                if (currentDraft.id.isBlank()) {
                    firebaseRepository?.addGalleryWork(currentDraft)
                } else {
                    firebaseRepository?.updateGalleryWork(currentDraft)
                }
            }
        }
    }

    fun loginAdmin(username: String, password: String) {
        val normalizedUsername = username.trim()
        val normalizedPassword = password.trim()

        if (firebaseRepository == null) {
            adminLoggedIn = false
            adminLoginMessage = "Firebase is not enabled for admin login yet."
            return
        }

        viewModelScope.launch {
            adminLoginInProgress = true
            adminLoginMessage = "Signing in..."
            adminLoggedIn = firebaseRepository.signInAdmin(normalizedUsername, normalizedPassword)
            adminLoginInProgress = false
            if (adminLoggedIn) {
                firebaseStatus = "Connected to Firebase Auth"
                adminLoginMessage = "Logged in with Firebase admin account."
            } else {
                firebaseStatus = "Firebase login failed"
                adminLoginMessage = "Login failed. Create a valid email/password admin user in Firebase Authentication and sign in with that account."
            }
        }
    }

    fun editGalleryWork(item: GalleryItem) {
        workDraft = WorkDraft(
            id = item.id,
            title = item.title,
            category = item.category,
            location = item.location,
            priceRange = item.priceRange,
            duration = item.completionDays,
            description = item.description,
            imageUrl = item.imageUrl
        )
        adminWorkMessage = "Editing ${item.title}."
    }

    fun deleteGalleryWork(id: String) {
        gallery.removeAll { it.id == id }
        if (workDraft.id == id) {
            workDraft = WorkDraft()
        }
        adminWorkMessage = "Work deleted."
        viewModelScope.launch {
            runCatching { firebaseRepository?.deleteGalleryWork(id) }
        }
    }

    fun clearWorkDraft() {
        workDraft = WorkDraft()
        adminWorkMessage = "Ready to add a new work item."
    }

    private fun refreshFromFirebase() {
        if (firebaseRepository == null) return
        viewModelScope.launch {
            runCatching { firebaseRepository.loadDashboardBundle() }
                .onSuccess { bundle ->
                    services.clear()
                    services.addAll(bundle.services)
                    metrics.clear()
                    metrics.addAll(bundle.metrics)
                    gallery.clear()
                    gallery.addAll(bundle.gallery)
                    inquiries.clear()
                    inquiries.addAll(bundle.inquiries)
                    billing.clear()
                    billing.addAll(bundle.billing)
                    firebaseStatus = "Loaded live Firestore data"
                }
                .onFailure {
                    firebaseStatus = "Firebase available, using fallback demo data"
                }
        }
    }
}
