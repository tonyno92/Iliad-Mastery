package com.iliadmastery.demo.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument

/**
 * Navigable screens from the navigator component
 */
sealed class Screen(val route: String, val arguments: List<NamedNavArgument>){

    object CountryList: Screen(
        route = "countryList",
        arguments = emptyList()
    )

    object CountryDetail: Screen(
        route = "countryDetail",
        arguments = listOf(navArgument("countryId") {
            type = NavType.IntType
        })
    )

}

