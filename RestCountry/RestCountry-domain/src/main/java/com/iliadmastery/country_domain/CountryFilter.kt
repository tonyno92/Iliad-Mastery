package com.iliadmastery.country_domain

import com.iliadmastery.core.domain.Filter
import com.iliadmastery.core.domain.FilterOrder

/**
 * Class representing a country's filter
 *
 * @param name : naming to show on ui
 */
sealed class CountryFilter(val name: String) : Filter {

    override val uiValue: String = name
    override val abbreviation: String = ""

    data class Country(
        val order: FilterOrder = FilterOrder.Descending
    ) : CountryFilter("Country")

}





