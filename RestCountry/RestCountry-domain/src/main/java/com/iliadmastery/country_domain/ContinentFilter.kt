package com.iliadmastery.country_domain

import com.iliadmastery.core.domain.Filter

/**
 * Class representing a continent filter
 *
 *
 * @param name : naming to show on ui
 * @param shorthand : abbreviation to show on ui
 */
sealed class ContinentFilter(
    name: String,
    shorthand: String,
) : Filter {

    override val uiValue: String = name
    override val abbreviation: String = shorthand

    companion object {
        val allContinents = arrayOf(
            Europe,
            NorthAmerica,
            SouthAmerica,
            Asia,
            Africa,
            Oceania
        )
    }

    object Europe : ContinentFilter(
        name = "Europe",
        shorthand = "eu",
    )

    object NorthAmerica : ContinentFilter(
        name = "North America",
        shorthand = "nam",
    )

    object SouthAmerica : ContinentFilter(
        name = "South America",
        shorthand = "sam",
    )

    object Oceania : ContinentFilter(
        name = "Oceania",
        shorthand = "oce",
    )

    object Asia : ContinentFilter(
        name = "Asia",
        shorthand = "asi",
    )

    object Africa : ContinentFilter(
        name = "Africa",
        shorthand = "afr",
    )

    object None : ContinentFilter(
        name = "None",
        shorthand = "n/a",
    )
}