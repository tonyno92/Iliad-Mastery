package com.iliadmastery.country_domain

import com.iliadmastery.core.domain.Filter

/**
 * Class representing a language filter
 *
 * @param name : naming to show on ui
 * @param shorthand : abbreviation to show on ui
 */
sealed class LanguageFilter(
    name: String,
    shorthand: String,
) : Filter {

    override val uiValue: String = name
    override val abbreviation: String = shorthand

    data class Language(
        val name: String = ""
    ) : LanguageFilter(name, "")

    /**
     * Nothing language selected
     */
    object None : LanguageFilter(
        name = "None",
        shorthand = "n/a",
    )
}
