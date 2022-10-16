package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_datasource_test.cache.CountryCacheFake
import com.iliadmastery.country_datasource_test.cache.CountryDatabaseFake
import com.iliadmastery.country_datasource_test.network.CountryServiceFake
import com.iliadmastery.country_datasource_test.network.CountryServiceResponseType
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid.NUM_COUNTRIES
import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_datasource_test.network.serializeCountryData
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * 1. Success (Retrieve a list of countries)
 * 2. Failure (Retrieve an empty list of countries)
 * 3. Failure (Retrieve malformed data (empty cache))
 * 4. Success (Retrieve malformed data but still returns data from cache)
 */
class GetCountriesTest {

    // system in test
    private lateinit var getCountries: GetCountries


    @Test
    fun getCountries_success() = runBlocking {

        // setup
        val countryDatabase = CountryDatabaseFake()
        val countryCache = CountryCacheFake(countryDatabase)
        val countryService = CountryServiceFake.build(
            type = CountryServiceResponseType.CorrectData // http 200 data ok
        )


        getCountries = GetCountries(
            cache = countryCache,
            service = countryService
        )

        // Confirm the cache is empty before any use-cases have been executed
        var cachedCountries = countryCache.selectAll()
        assert(cachedCountries.isEmpty())

        // Execute the use-case
        val emissions = getCountries.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<List<Country>>(ProgressBarState.Loading))

        // Confirm second emission is data
        assert(emissions[1] is DataState.Data)
        assert(((emissions[1] as DataState.Data).data?.size ?: 0) == NUM_COUNTRIES)

        // Confirm the cache is no longer empty
        cachedCountries = countryCache.selectAll()
        assert(cachedCountries.size == NUM_COUNTRIES)

        // Confirm loading state is IDLE
        assert(emissions[2] == DataState.Loading<List<Country>>(ProgressBarState.Idle))
    }

    @Test
    fun getCountries_emptyList() = runBlocking {
        // setup
        val countryDatabase = CountryDatabaseFake()
        val countryCache = CountryCacheFake(countryDatabase)
        val countryService = CountryServiceFake.build(
            type = CountryServiceResponseType.EmptyList // Empty List
        )

        getCountries = GetCountries(
            cache = countryCache,
            service = countryService
        )

        // Confirm the cache is empty before any use-cases have been executed
        var cachedCountries = countryCache.selectAll()
        assert(cachedCountries.isEmpty())

        // Execute the use-case
        val emissions = getCountries.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<List<Country>>(ProgressBarState.Loading))

        // Confirm second emission is data (empty list)
        assert(emissions[1] is DataState.Data)
        assert(((emissions[1] as DataState.Data).data?.size ?: 0) == 0)

        // Confirm the cache is STILL EMPTY
        cachedCountries = countryCache.selectAll()
        assert(cachedCountries.size == 0)

        // Confirm loading state is IDLE
        assert(emissions[2] == DataState.Loading<List<Country>>(ProgressBarState.Idle))
    }

    @Test
    fun getCountries_malformedData() = runBlocking {
        // setup
        val countryDatabase = CountryDatabaseFake()
        val countryCache = CountryCacheFake(countryDatabase)
        val countryService = CountryServiceFake.build(
            type = CountryServiceResponseType.ErrorOnJsonData // Malformed data
        )

        getCountries = GetCountries(
            cache = countryCache,
            service = countryService
        )

        // Confirm the cache is empty before any use-cases have been executed
        var cachedCountries = countryCache.selectAll()
        assert(cachedCountries.isEmpty())

        // Execute the use-case
        val emissions = getCountries.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<List<Country>>(ProgressBarState.Loading))

        // Confirm second emission is error response
        assert(emissions[1] is DataState.Response)
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).title == "Network Data Error")
        assert(
            ((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).description.contains(
                "Unexpected JSON token at offset"
            )
        )

        // Confirm third emission is empty list of data
        assert(emissions[2] is DataState.Data)
        assert((emissions[2] as DataState.Data).data?.size == 0)

        // Confirm the cache is STILL EMPTY
        cachedCountries = countryCache.selectAll()
        assert(cachedCountries.isEmpty())

        // Confirm loading state is IDLE
        assert(emissions[3] == DataState.Loading<List<Country>>(ProgressBarState.Idle))
    }

    /**
     * 1. Insert some data into the cache by executing a successful use-case.
     * 2. Configure the network operation to return malformed data.
     * 3. Execute use-case for a second time and confirm it still emits the cached data.
     */
    @Test
    fun getCountries_malformedData_successFromCache() = runBlocking {
        // setup
        val countryDatabase = CountryDatabaseFake()
        val countryCache = CountryCacheFake(countryDatabase)
        val countryService = CountryServiceFake.build(
            type = CountryServiceResponseType.ErrorOnJsonData // Malformed Data
        )

        getCountries = GetCountries(
            cache = countryCache,
            service = countryService
        )

        // Confirm the cache is empty before any use-cases have been executed
        var cachedCountries = countryCache.selectAll()
        assert(cachedCountries.isEmpty())

        // Add some data to the cache by executing a successful request
        val countryData = serializeCountryData(CountryDataValid.data)
        countryCache.insert(countryData)

        // Confirm the cache is not empty anymore
        cachedCountries = countryCache.selectAll()
        assert(cachedCountries.size == NUM_COUNTRIES)

        // Execute the use-case
        val emissions = getCountries.execute().toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<List<Country>>(ProgressBarState.Loading))

        // Confirm second emission is error response
        assert(emissions[1] is DataState.Response)
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).title == "Network Data Error")
        assert(
            ((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).description.contains(
                "Unexpected JSON token at offset"
            )
        )

        // Confirm third emission is data from the cache
        assert(emissions[2] is DataState.Data)
        assert((emissions[2] as DataState.Data).data?.size == NUM_COUNTRIES)

        // Confirm the cache is still not empty
        cachedCountries = countryCache.selectAll()
        assert(cachedCountries.size == NUM_COUNTRIES)

        // Confirm loading state is IDLE
        assert(emissions[3] == DataState.Loading<List<Country>>(ProgressBarState.Idle))
    }

}

















