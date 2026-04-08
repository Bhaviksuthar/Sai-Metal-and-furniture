package com.saimetal.furniture.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.MeetingRoom
import androidx.compose.material.icons.rounded.Stairs
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saimetal.furniture.data.model.BillingRecord
import com.saimetal.furniture.data.model.DashboardMetric
import com.saimetal.furniture.data.model.GalleryItem
import com.saimetal.furniture.data.model.ServiceCategory

@Composable
fun SectionTitle(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun MetricCard(metric: DashboardMetric, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = metric.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = metric.value, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = metric.note, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ServiceCard(service: ServiceCategory, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = serviceIcon(service.icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = service.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = service.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = service.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AssistChip(onClick = {}, label = { Text(service.startingPrice) })
                Icon(imageVector = Icons.Rounded.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
fun GalleryCard(item: GalleryItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(item.accent))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = item.category, style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.84f))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = item.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.92f))
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.tags.take(2).forEach { tag ->
                    AssistChip(onClick = {}, label = { Text(tag) })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${item.location}  •  ${item.completionDays}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.priceRange, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}

@Composable
fun BillingCard(record: BillingRecord, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = record.clientName, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = record.projectTitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Total: ${record.totalAmount}", style = MaterialTheme.typography.bodyMedium)
                Text(text = record.paymentStatus, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Advance: ${record.advancePaid}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Due: ${record.dueAmount}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Due date: ${record.dueDate}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun serviceIcon(icon: String): ImageVector = when (icon) {
    "gate" -> Icons.Rounded.MeetingRoom
    "stairs" -> Icons.Rounded.Stairs
    "bed" -> Icons.Rounded.Checkroom
    "office" -> Icons.Rounded.Inventory2
    else -> Icons.Rounded.Construction
}
