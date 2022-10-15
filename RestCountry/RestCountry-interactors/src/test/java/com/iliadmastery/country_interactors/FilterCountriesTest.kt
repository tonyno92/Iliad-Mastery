package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.FilterOrder
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import com.iliadmastery.country_datasource_test.network.serializeCountryData
import com.iliadmastery.country_domain.ContinentFilter
import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_domain.CountryFilter
import com.iliadmastery.country_domain.LanguageFilter
import org.junit.Before
import org.junit.Test

/**
 * 1. Success (Search for specific country by 'name' param)
 * 2. Success (Order by 'name' param DESC)
 * 3. Success (Order by 'name' param ASC)
 * 4. Success (Filter by 'ContinentFilter' "Chosen at random")
 * 5. Success (Filter by 'LanguageFilter' "Italian")
 */
class FilterCountriesTest {

    // System in test
    private lateinit var filterCountries: FilterCountries

    // Data
    private val countryList = serializeCountryData(CountryDataValid.data)

    @Before
    fun setup() {
        filterCountries = FilterCountries()
    }

    @Test
    fun searchCountryByName() {

        // Run use-case
        val emissionStream = filterCountries.execute(
            current = countryList,
            countryName = "Italian Republic",
            countryFilter = CountryFilter.Country(),
            continentFilter = ContinentFilter.None,
            languageFilter = LanguageFilter.None
        )

        // confirm it returns a single country
        assert(emissionStream[0].name == "Italian Republic")
    }

    @Test
    fun orderCountriesByNameDesc() {

        // Run use-case
        val emissionStream = filterCountries.execute(
            current = countryList,
            countryName = "",
            countryFilter = CountryFilter.Country(order = FilterOrder.Descending),
            continentFilter = ContinentFilter.None,
            languageFilter = LanguageFilter.None
        )

        // confirm they are ordered by descending
        emissionStream.onEachIndexed { index: Int, country: Country ->
            if (index > 0) {
                assert(emissionStream[index - 1].name.first() >= country.name.first())
            }
        }
        (1 until emissionStream.size).onEach { index ->
            assert(emissionStream[index - 1].name.first() >= emissionStream[index].name.first())
        }
    }

    @Test
    fun orderCountriesByNameAsc() {

        // Run use-case
        val emissionStream = filterCountries.execute(
            current = countryList,
            countryName = "",
            countryFilter = CountryFilter.Country(order = FilterOrder.Ascending),
            continentFilter = ContinentFilter.None,
            languageFilter = LanguageFilter.None
        )

        // confirm they are ordered by aescending
        (1 until emissionStream.size).onEach { index ->
            assert(emissionStream[index - 1].name.first() <= emissionStream[index].name.first())
        }
    }

    @Test
    fun filterCountriesByContinent() {

        // Choose random continent
        val continentFilter = ContinentFilter.allContinents.random()

        // Run use-case
        val emissions = filterCountries.execute(
            current = countryList,
            countryName = "",
            countryFilter = CountryFilter.Country(),
            continentFilter = continentFilter,
            languageFilter = LanguageFilter.None,
        )

        for (country in emissions) {
            assert(country.continents.contains(continentFilter.uiValue))
        }
    }

    @Test
    fun filterCountriesByLanguage() {

        // Choose a languge
        val languageFilter = LanguageFilter.Language("Italian")

        // Run use-case
        val emissions = filterCountries.execute(
            current = countryList,
            countryName = "",
            countryFilter = CountryFilter.Country(),
            continentFilter = ContinentFilter.None,
            languageFilter = languageFilter,
        )

        for (country in emissions) {
            assert(country.languages.contains(languageFilter.uiValue))
        }
    }

}
















