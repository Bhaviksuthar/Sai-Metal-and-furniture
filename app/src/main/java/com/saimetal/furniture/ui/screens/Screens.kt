package com.saimetal.furniture.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.saimetal.furniture.ui.SaiMetalViewModel
import com.saimetal.furniture.ui.components.BillingCard
import com.saimetal.furniture.ui.components.GalleryCard
import com.saimetal.furniture.ui.components.MetricCard
import com.saimetal.furniture.ui.components.SectionTitle
import com.saimetal.furniture.ui.components.ServiceCard
import com.saimetal.furniture.ui.theme.Copper
import com.saimetal.furniture.ui.theme.Iron
import com.saimetal.furniture.ui.theme.Sand
import com.saimetal.furniture.ui.theme.Steel

@Composable
fun HomeScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Iron, Steel, Copper.copy(alpha = 0.86f))
                            ),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        AssistChip(onClick = {}, label = { Text("Since 2011 - Custom fabrication") })
                        Spacer(modifier = Modifier.height(18.dp))
                        Text("Sai Metal And Furniture", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Beautifully finished metal works and furniture for homes, offices, showrooms, and custom interior projects.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilledTonalButton(onClick = {}) {
                                Icon(Icons.Rounded.Call, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Call Owner")
                            }
                            FilledTonalButton(onClick = {}) {
                                Icon(Icons.Rounded.Message, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("WhatsApp")
                            }
                        }
                    }
                }
            }
        }
        item {
            SectionTitle(
                title = "Business Snapshot",
                subtitle = "A customer-first profile with admin-ready insights."
            )
        }
        items(viewModel.metrics) { metric ->
            MetricCard(metric = metric)
        }
        item {
            SectionTitle(
                title = "Featured Works",
                subtitle = "Recent highlights to help clients imagine their own project."
            )
        }
        items(viewModel.gallery.take(3)) { item ->
            GalleryCard(item = item)
        }
    }
}

@Composable
fun GalleryScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle(
                title = "Project Gallery",
                subtitle = "Curated examples of gates, railings, furniture, and commercial work."
            )
        }
        items(viewModel.gallery) { item ->
            GalleryCard(item = item)
        }
    }
}

@Composable
fun ServicesScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle(
                title = "Services",
                subtitle = "Designed to showcase the business clearly to customers and builders."
            )
        }
        items(viewModel.services) { service ->
            ServiceCard(service = service)
        }
    }
}

@Composable
fun QuoteScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionTitle(
            title = "Request a Quote",
            subtitle = "Let customers reach the business directly with project details, budget, and contact information."
        )
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Sand)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = viewModel.quoteDraft.customerName,
                    onValueChange = { viewModel.updateQuoteDraft { copy(customerName = it) } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Customer name") }
                )
                OutlinedTextField(
                    value = viewModel.quoteDraft.phone,
                    onValueChange = { viewModel.updateQuoteDraft { copy(phone = it) } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Phone number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                OutlinedTextField(
                    value = viewModel.quoteDraft.workType,
                    onValueChange = { viewModel.updateQuoteDraft { copy(workType = it) } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Work type") }
                )
                OutlinedTextField(
                    value = viewModel.quoteDraft.budget,
                    onValueChange = { viewModel.updateQuoteDraft { copy(budget = it) } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Budget") }
                )
                OutlinedTextField(
                    value = viewModel.quoteDraft.message,
                    onValueChange = { viewModel.updateQuoteDraft { copy(message = it) } },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    label = { Text("Project message") }
                )
                Button(onClick = { viewModel.submitInquiry() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Submit inquiry")
                }
            }
        }
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Quick contact", style = MaterialTheme.typography.titleLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Call, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("+91 98765 43210")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Workshop and site service across South Gujarat")
                }
            }
        }
    }
}

@Composable
fun AdminScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    if (!viewModel.adminLoggedIn) {
        AdminLoginScreen(viewModel = viewModel, modifier = modifier)
    } else {
        AdminDashboardScreen(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
private fun AdminLoginScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Iron, Steel)))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f))
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier
                        .background(Copper.copy(alpha = 0.16f), CircleShape)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Rounded.AdminPanelSettings, contentDescription = null, tint = Copper)
                }
                Text("Owner Login", style = MaterialTheme.typography.headlineMedium)
                Text("Demo login: owner@sai / 123456", style = MaterialTheme.typography.bodyMedium)
                Text("Firebase login: use the email/password user created in Firebase Authentication.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username or email") })
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                if (viewModel.adminLoginInProgress) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                Text(
                    text = viewModel.adminLoginMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = { viewModel.loginAdmin(username, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enter admin panel")
                }
            }
        }
    }
}

@Composable
private fun AdminDashboardScreen(viewModel: SaiMetalViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        SectionTitle(
            title = "Admin Dashboard",
            subtitle = "Owners can manage works, inquiries, and billing from one place."
        )
        viewModel.metrics.forEach { metric ->
            MetricCard(metric = metric)
        }
        HorizontalDivider()
        Text("Add New Gallery Work", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = viewModel.workDraft.title,
            onValueChange = { viewModel.updateWorkDraft { copy(title = it) } },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Work title") }
        )
        OutlinedTextField(
            value = viewModel.workDraft.category,
            onValueChange = { viewModel.updateWorkDraft { copy(category = it) } },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category") }
        )
        OutlinedTextField(
            value = viewModel.workDraft.location,
            onValueChange = { viewModel.updateWorkDraft { copy(location = it) } },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Location") }
        )
        OutlinedTextField(
            value = viewModel.workDraft.priceRange,
            onValueChange = { viewModel.updateWorkDraft { copy(priceRange = it) } },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Price range") }
        )
        OutlinedTextField(
            value = viewModel.workDraft.duration,
            onValueChange = { viewModel.updateWorkDraft { copy(duration = it) } },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Completion time") }
        )
        OutlinedTextField(
            value = viewModel.workDraft.description,
            onValueChange = { viewModel.updateWorkDraft { copy(description = it) } },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            label = { Text("Description") }
        )
        Button(onClick = { viewModel.addGalleryWork() }, modifier = Modifier.fillMaxWidth()) {
            Text("Save work to gallery")
        }
        HorizontalDivider()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.WorkspacePremium, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recent Inquiries", style = MaterialTheme.typography.titleLarge)
        }
        viewModel.inquiries.forEach { inquiry ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${inquiry.customerName} - ${inquiry.workType}", style = MaterialTheme.typography.titleMedium)
                    Text(inquiry.message, style = MaterialTheme.typography.bodyMedium)
                    Text("${inquiry.phone} - ${inquiry.budget}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    AssistChip(onClick = {}, label = { Text(inquiry.status) })
                }
            }
        }
        HorizontalDivider()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Payments, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Billing Management", style = MaterialTheme.typography.titleLarge)
        }
        viewModel.billing.forEach { record ->
            BillingCard(record = record)
        }
    }
}
