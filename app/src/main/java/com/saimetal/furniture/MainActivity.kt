package com.saimetal.furniture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saimetal.furniture.ui.SaiMetalViewModel
import com.saimetal.furniture.ui.navigation.AppDestination
import com.saimetal.furniture.ui.screens.AboutScreen
import com.saimetal.furniture.ui.screens.AdminBillingScreen
import com.saimetal.furniture.ui.screens.AdminHomeScreen
import com.saimetal.furniture.ui.screens.AdminServicesScreen
import com.saimetal.furniture.ui.screens.AdminWorksScreen
import com.saimetal.furniture.ui.screens.FavoritesScreen
import com.saimetal.furniture.ui.screens.GalleryScreen
import com.saimetal.furniture.ui.screens.HomeScreen
import com.saimetal.furniture.ui.screens.QuoteScreen
import com.saimetal.furniture.ui.screens.ServicesScreen
import com.saimetal.furniture.ui.theme.SaiMetalTheme

enum class AppMode {
    ROLE_PICK,
    CUSTOMER,
    ADMIN
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaiMetalTheme {
                SaiMetalApp()
            }
        }
    }
}

@Composable
fun SaiMetalApp(viewModel: SaiMetalViewModel = viewModel()) {
    var appMode by rememberSaveable {
        mutableStateOf(if (viewModel.adminLoggedIn) AppMode.ADMIN else AppMode.ROLE_PICK)
    }

    when (appMode) {
        AppMode.ROLE_PICK -> RoleSelectionScreen(
            onCustomerSelected = { appMode = AppMode.CUSTOMER },
            onAdminSelected = { appMode = AppMode.ADMIN }
        )
        AppMode.CUSTOMER -> CustomerShell(viewModel = viewModel, onSwitchRole = { appMode = AppMode.ROLE_PICK })
        AppMode.ADMIN -> AdminShell(viewModel = viewModel, onSwitchRole = { appMode = AppMode.ROLE_PICK })
    }
}

@Composable
private fun RoleSelectionScreen(
    onCustomerSelected: () -> Unit,
    onAdminSelected: () -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Sai Metal And Furniture", style = MaterialTheme.typography.headlineLarge)
            Text("Choose how you want to enter the app.", style = MaterialTheme.typography.bodyLarge)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Customer", style = MaterialTheme.typography.titleLarge)
                    Text("Browse works, services, business details, and request a quote.", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onCustomerSelected) { Text("Continue as customer") }
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Admin", style = MaterialTheme.typography.titleLarge)
                    Text("Sign in to manage works, billing, inquiries, and images.", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onAdminSelected) { Text("Continue as admin") }
                }
            }
        }
    }
}

@Composable
private fun CustomerShell(viewModel: SaiMetalViewModel, onSwitchRole: () -> Unit) {
    val items = listOf(
        AppDestination.Home,
        AppDestination.Gallery,
        AppDestination.Services,
        AppDestination.Favorites,
        AppDestination.Quote,
        AppDestination.About
    )
    var currentRoute by remember { mutableStateOf(AppDestination.Home.route) }

    BaseShell(items, currentRoute, { currentRoute = it }, onSwitchRole) {
        when (currentRoute) {
            AppDestination.Home.route -> HomeScreen(viewModel = viewModel)
            AppDestination.Gallery.route -> GalleryScreen(viewModel = viewModel)
            AppDestination.Services.route -> ServicesScreen(viewModel = viewModel)
            AppDestination.Favorites.route -> FavoritesScreen(viewModel = viewModel)
            AppDestination.Quote.route -> QuoteScreen(viewModel = viewModel)
            AppDestination.About.route -> AboutScreen(viewModel = viewModel)
        }
    }
}

@Composable
private fun AdminShell(viewModel: SaiMetalViewModel, onSwitchRole: () -> Unit) {
    val items = listOf(
        AppDestination.AdminHome,
        AppDestination.AdminServices,
        AppDestination.AdminWorks,
        AppDestination.AdminBilling
    )
    var currentRoute by remember { mutableStateOf(AppDestination.AdminHome.route) }

    BaseShell(items, currentRoute, { currentRoute = it }, onSwitchRole) {
        when (currentRoute) {
            AppDestination.AdminHome.route -> AdminHomeScreen(viewModel = viewModel)
            AppDestination.AdminServices.route -> AdminServicesScreen(viewModel = viewModel)
            AppDestination.AdminWorks.route -> AdminWorksScreen(viewModel = viewModel)
            AppDestination.AdminBilling.route -> AdminBillingScreen(viewModel = viewModel)
        }
    }
}

@Composable
private fun BaseShell(
    items: List<AppDestination>,
    currentRoute: String,
    onRouteSelected: (String) -> Unit,
    onSwitchRole: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScrollableTabRow(
                selectedTabIndex = items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
            ) {
                items.forEach { destination ->
                    Tab(
                        selected = currentRoute == destination.route,
                        onClick = { onRouteSelected(destination.route) },
                        text = { Text(destination.label) }
                    )
                }
                Tab(selected = false, onClick = onSwitchRole, text = { Text("Switch") })
            }
            content()
        }
    }
}
