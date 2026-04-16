package com.saimetal.furniture.data.model

data class ServiceCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val startingPrice: String,
    val icon: String,
    val imageUrl: String = ""
)

data class GalleryItem(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val description: String,
    val priceRange: String,
    val completionDays: String,
    val tags: List<String>,
    val accent: Long,
    val imageUrl: String = ""
)

data class Inquiry(
    val id: String,
    val customerName: String,
    val phone: String,
    val workType: String,
    val message: String,
    val status: String,
    val budget: String
)

data class BillingRecord(
    val id: String,
    val clientName: String,
    val projectTitle: String,
    val totalAmount: String,
    val advancePaid: String,
    val dueAmount: String,
    val dueDate: String,
    val paymentStatus: String
)

data class BillingDraft(
    val id: String = "",
    val clientName: String = "",
    val projectTitle: String = "",
    val totalAmount: String = "",
    val advancePaid: String = "Not Paid",
    val dueAmount: String = "",
    val dueDate: String = "",
    val paymentStatus: String = "Unpaid"
)

data class ServiceDraft(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val startingPrice: String = "",
    val icon: String = "office",
    val imageUrl: String = ""
)

data class DashboardMetric(
    val title: String,
    val value: String,
    val note: String
)

data class QuoteDraft(
    val customerName: String = "",
    val phone: String = "",
    val workType: String = "",
    val budget: String = "",
    val message: String = ""
)

data class WorkDraft(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val location: String = "",
    val priceRange: String = "",
    val duration: String = "",
    val description: String = "",
    val imageUrl: String = ""
)
