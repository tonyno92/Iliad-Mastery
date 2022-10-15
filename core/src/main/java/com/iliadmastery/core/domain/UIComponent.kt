package com.iliadmastery.core.domain

/**
 * Sealed class uses for shape ui view element as data class
 */
sealed class UIComponent{

    data class Dialog(
        val title: String,
        val description: String,
    ): UIComponent()

    data class None(
        val message: String,
    ): UIComponent()

    // Add more here
    // ...
}







