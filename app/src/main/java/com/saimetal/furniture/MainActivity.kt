package com.saimetal.furniture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saimetal.furniture.ui.SaiMetalViewModel
import com.saimetal.furniture.ui.navigation.AppDestination
import com.saimetal.furniture.ui.screens.AdminScreen
import com.saimetal.furniture.ui.screens.AboutScreen
import com.saimetal.furniture.ui.screens.BillingOverviewScreen
import com.saimetal.furniture.ui.screens.FavoritesScreen
import com.saimetal.furniture.ui.screens.GalleryScreen
import com.saimetal.furniture.ui.screens.HomeScreen
import com.saimetal.furniture.ui.screens.QuoteScreen
import com.saimetal.furniture.ui.screens.ServicesScreen
import com.saimetal.furniture.ui.theme.SaiMetalTheme

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
    val items = listOf(
        AppDestination.Home,
        AppDestination.Gallery,
        AppDestination.Services,
        AppDestination.Favorites,
        AppDestination.Quote,
        AppDestination.About,
        AppDestination.Billing,
        AppDestination.Admin
    )
    var currentRoute by remember { mutableStateOf(AppDestination.Home.route) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScrollableTabRow(
                selectedTabIndex = items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
            ) {
                items.forEach { destination ->
                    Tab(
                        selected = currentRoute == destination.route,
                        onClick = { currentRoute = destination.route },
                        text = { Text(destination.label) }
                    )
                }
            }
            when (currentRoute) {
                AppDestination.Home.route -> HomeScreen(viewModel = viewModel)
                AppDestination.Gallery.route -> GalleryScreen(viewModel = viewModel)
                AppDestination.Services.route -> ServicesScreen(viewModel = viewModel)
                AppDestination.Favorites.route -> FavoritesScreen(viewModel = viewModel)
                AppDestination.Quote.route -> QuoteScreen(viewModel = viewModel)
                AppDestination.About.route -> AboutScreen(viewModel = viewModel)
                AppDestination.Billing.route -> BillingOverviewScreen(viewModel = viewModel)
                AppDestination.Admin.route -> AdminScreen(viewModel = viewModel)
                else -> HomeScreen(viewModel = viewModel)
            }
        }
    }
}
