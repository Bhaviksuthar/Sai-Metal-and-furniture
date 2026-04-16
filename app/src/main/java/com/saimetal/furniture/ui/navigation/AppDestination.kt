package com.saimetal.furniture.ui.navigation

sealed class AppDestination(val route: String, val label: String) {
    data object Home : AppDestination("home", "Home")
    data object Gallery : AppDestination("gallery", "Gallery")
    data object Services : AppDestination("services", "Services")
    data object Favorites : AppDestination("favorites", "Saved")
    data object Quote : AppDestination("quote", "Quote")
    data object About : AppDestination("about", "About")
    data object AdminHome : AppDestination("admin_home", "Overview")
    data object AdminWorks : AppDestination("admin_works", "Works")
    data object AdminBilling : AppDestination("admin_billing", "Billing")
}
