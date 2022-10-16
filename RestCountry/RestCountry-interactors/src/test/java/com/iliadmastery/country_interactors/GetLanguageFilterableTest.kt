package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_datasource_test.cache.CountryCacheFake
import com.iliadmastery.country_datasource_test.cache.CountryDatabaseFake
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid.NUM_LANGUAGES
import com.iliadmastery.country_datasource_test.network.serializeCountryData
import com.iliadmastery.country_domain.Country
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 *
 * 1. Success (Retrieve all languages filterable from the cache successfully)
 * 2. Failure (The cache is empty or sudden cleaning)
 */
class GetLanguageFilterableTest {

    // system in test
    private lateinit var getLanguageFilterable: GetLanguageFilterable

    // test support objects
    private lateinit var countryCache: CountryCacheFake
    private lateinit var countryDatabase: CountryDatabaseFake
    private lateinit var countryData: List<Country>

    @Before
    fun setup() = runBlocking {
        countryDatabase = CountryDatabaseFake()
        countryCache = CountryCacheFake(countryDatabase)

        getLanguageFilterable = GetLanguageFilterable(countryCache)

        countryData = serializeCountryData(CountryDataValid.data)

    }

    @Test
    fun getLanguageFilterable_success() = runBlocking {

        // Populate cache
        countryCache.insert(countryData)

        // choose a country at random
        val country = countryData.random()

        // Run the use-case
        val emissionStream = getLanguageFilterable.execute().toList()

        // First emission should be loading
        assert(emissionStream.first() == DataState.Loading<Country>(ProgressBarState.Loading))

        // Confirm second emission is data from the cache
        assert(emissionStream[1] is DataState.Data)
        assert((emissionStream[1] as DataState.Data).data?.size == NUM_LANGUAGES)
        assert((emissionStream[1] as DataState.Data).data
            ?.any { lang -> country.languages.contains(lang.name) } ?: false
        )

        // Confirm loading state is IDLE
        assert(emissionStream.last() == DataState.Loading<Country>(ProgressBarState.Idle))
    }

    @Test
    fun getLanguageFilterable_fail() = runBlocking {

        // Populate cache
        countryCache.insert(countryData)

        // Simulate sudden cache cleaning
        countryData.onEach { countryCache.removeCountry(it.id) }

        // Execute the use-case
        val emissionStream = getLanguageFilterable.execute().toList()

        // First emission should be loading
        assert(emissionStream[0] == DataState.Loading<Country>(ProgressBarState.Loading))

        // Confirm second emission is error response
        assert(emissionStream[1] is DataState.Response)
        assert(
            ((emissionStream[1] as DataState.Response).uiComponent as UIComponent.Dialog)
                .title == "Error"
        )
        assert(
            ((emissionStream[1] as DataState.Response).uiComponent as UIComponent.Dialog)
                .description
                .contains("There aren't countries into cache.")
        )

        // Confirm loading state is IDLE
        assert(emissionStream[2] == DataState.Loading<Country>(ProgressBarState.Idle))
    }

}














