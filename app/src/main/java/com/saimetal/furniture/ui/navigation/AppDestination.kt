package com.saimetal.furniture.ui.navigation

sealed class AppDestination(val route: String, val label: String) {
    data object Home : AppDestination("home", "Home")
    data object Gallery : AppDestination("gallery", "Gallery")
    data object Services : AppDestination("services", "Services")
    data object Favorites : AppDestination("favorites", "Saved")
    data object Quote : AppDestination("quote", "Quote")
    data object About : AppDestination("about", "About")
    data object Billing : AppDestination("billing", "Billing")
    data object Admin : AppDestination("admin", "Admin")
}
