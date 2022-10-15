package com.iliadmastery.ui_countrylist.ui

import com.iliadmastery.core.domain.*
import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_domain.ContinentFilter
import com.iliadmastery.country_domain.CountryFilter
import com.iliadmastery.country_domain.LanguageFilter


/**
 * Possible states of Country List Screen
 *
 * @property countryName : Filter countries by their name containing this string.
 * @property countries : Unfiltered list of countries.
 * @property filteredCountries : Filtered list of countries.
 * @property countryFilter : Filter countries by 'name' (asc / desc) or 'pro wins' (asc / desc)
 * @property contientFilter : Filter countries by continent
 * @property languagesFilterable: Available languages that user can select to filter countries
 * @property languageFilter Filter countries by language
 * @property progressBarState : State of the progress bar in UI.
 * @property filterDialogState : Show/hide the dialog with filter options
 * @property errorQueue : Errors (will be shown as dialog or none and logged)
 */
data class CountryListState(
    val countryName: String = "",
    val countries: List<Country> = listOf(),
    val filteredCountries: List<Country> = listOf(),
    val countryFilter: CountryFilter = CountryFilter.Country(FilterOrder.Descending),
    val contientFilter: ContinentFilter = ContinentFilter.None,
    val languagesFilterable: List<LanguageFilter.Language> = emptyList(),
    val languageFilter: LanguageFilter = LanguageFilter.None,

    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val filterDialogState: UIComponentState = UIComponentState.Hide,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
)
