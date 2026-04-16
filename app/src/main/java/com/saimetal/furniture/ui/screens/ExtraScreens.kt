package com.saimetal.furniture.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Handyman
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saimetal.furniture.ui.SaiMetalViewModel
import com.saimetal.furniture.ui.components.BillingCard
import com.saimetal.furniture.ui.components.GalleryCard
import com.saimetal.furniture.ui.components.SectionTitle

@Composable
fun FavoritesScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    val favoriteItems = viewModel.gallery.take(2)
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle(
                title = "Saved Inspirations",
                subtitle = "A lightweight favorites view clients can use to shortlist styles before talking to the workshop."
            )
        }
        items(favoriteItems) { item ->
            GalleryCard(item = item)
        }
    }
}

@Composable
fun AboutScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle(
                title = "About Sai Metal And Furniture",
                subtitle = "A clear business profile screen to build trust with new customers."
            )
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                androidx.compose.foundation.layout.Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    InfoRow(Icons.Rounded.Handyman, "Expertise", "Custom metal fabrication, modular furniture, and commercial interiors.")
                    InfoRow(Icons.Rounded.Groups, "Owners", "Ramesh Suthar, Jitu Suthar, Himmat Suthar")
                    InfoRow(Icons.Rounded.Place, "Address", "B-6 Sainath Industrial Society, Bhatar Road, beside CNG pump, Surat, Gujarat, 395017")
                    InfoRow(Icons.Rounded.Place, "Service Area", "Surat and surrounding Gujarat project locations.")
                    InfoRow(Icons.Rounded.CheckCircle, "Why Choose Us", "On-site measurement, tailored designs, durable materials, and neat finishing.")
                    InfoRow(Icons.Rounded.Payments, "Working Style", "Quotation first, milestone billing, and delivery updates for each project.")
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                androidx.compose.foundation.layout.Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("System status", style = MaterialTheme.typography.titleLarge)
                    Text(viewModel.firebaseStatus, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    Text("Sai Metal And Furniture specializes in all types of metal and furniture works for homes, offices, shops, and custom fabrication projects.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun BillingOverviewScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle(
                title = "Billing Overview",
                subtitle = "A clean financial screen owners can use to track due amounts and customer payment progress."
            )
        }
        items(viewModel.billing) { record ->
            BillingCard(record = record)
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        androidx.compose.foundation.layout.Column {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
