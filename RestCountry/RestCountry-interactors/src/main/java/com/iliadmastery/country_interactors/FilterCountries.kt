package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.Filter
import com.iliadmastery.core.domain.FilterOrder
import com.iliadmastery.country_domain.*

/**
 * Rappresenting a Use-Case
 * Also even though it does not access any datasources i created a use-case
 * This helps to keep the filtering logic isolated.
 */
class FilterCountries {

    fun execute(
        current: List<Country>,
        countryName: String,
        countryFilter: Filter,
        continentFilter: Filter,
        languageFilter: Filter,
    ): List<Country> {
        var filteredList: MutableList<Country> = current.filter {
            it.name.lowercase().contains(countryName.lowercase())
        }.toMutableList()

        when (countryFilter) {
            is CountryFilter.Country -> {
                when (countryFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending { it.name }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy { it.name }
                    }
                }
            }
        }

        // Filter by continent
        when (continentFilter) {

            is ContinentFilter.None -> {
                // don't filter
            }
            else -> {
                filteredList = filteredList.filter { country ->
                    country.continents.any { name ->
                        name.lowercase()
                            .contains(continentFilter.uiValue.lowercase(), ignoreCase = true)
                    }
                }.toMutableList()
            }
        }

        // Filter by language
        when (languageFilter) {
            is LanguageFilter.Language -> {
                filteredList = filteredList.filter { country ->
                    country.languages.any { name ->
                        name.lowercase()
                            .contains(languageFilter.uiValue.lowercase(), ignoreCase = true)
                    }
                }.toMutableList()
            }
            LanguageFilter.None -> {
                // don't filter
            }
        }

        return filteredList
    }
}