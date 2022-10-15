package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_datasource_test.cache.CountryCacheFake
import com.iliadmastery.country_datasource_test.cache.CountryDatabaseFake
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import com.iliadmastery.country_datasource_test.network.serializeCountryData
import com.iliadmastery.country_domain.Country
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

/**
 *
 * 1. Success (Retrieve a country from the cache successfully)
 * 2. Failure (The country doesn't exist in the cache)
 */
class GetCountryFromCacheTest {

    // system in test
    private lateinit var getCountryFromCache: GetCountryFromCache

    // test support objects
    private lateinit var countryCache: CountryCacheFake
    private lateinit var countryDatabase: CountryDatabaseFake
    private lateinit var countryData: List<Country>

    @Before
    fun setup() = runBlocking {
        countryDatabase = CountryDatabaseFake()
        countryCache = CountryCacheFake(countryDatabase)

        getCountryFromCache = GetCountryFromCache(countryCache)

        countryData = serializeCountryData(CountryDataValid.data).simulateIdAutoIncrement()
    }

    @Test
    fun getCountryFromCache_success() = runBlocking {

        countryCache.insert(countryData)

        // choose a country at random
        val country = countryData[Random.nextInt(0, countryData.size - 1)]

        // Execute the use-case
        val emissionStream = getCountryFromCache.execute(country.id).toList()

        // First emission should be loading
        assert(emissionStream[0] == DataState.Loading<Country>(ProgressBarState.Loading))

        // Confirm second emission is data from the cache
        assert(emissionStream[1] is DataState.Data)
        assert((emissionStream[1] as DataState.Data).data?.id == country.id)
        assert((emissionStream[1] as DataState.Data).data?.name == country.name)

        // Confirm loading state is IDLE
        assert(emissionStream[2] == DataState.Loading<Country>(ProgressBarState.Idle))
    }

    @Test
    fun getCountryFromCache_fail() = runBlocking {

        countryCache.insert(countryData)

        // choose a country at random and remove it from the cache
        val country = countryData[Random.nextInt(0, countryData.size - 1)]
        countryCache.removeCountry(country.id)

        // Execute the use-case
        val emissionStream = getCountryFromCache.execute(country.id).toList()

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
                .contains("That country does not exist in the cache.")
        )

        // Confirm loading state is IDLE
        assert(emissionStream[2] == DataState.Loading<Country>(ProgressBarState.Idle))
    }
}














