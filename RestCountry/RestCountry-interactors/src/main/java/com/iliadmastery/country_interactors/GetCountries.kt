package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_datasource.cache.CountryCache
import com.iliadmastery.country_datasource.network.CountryService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Rappresenting a Use-Case
 *
 * 1. Retrieve the countries from the network
 * 2. Cache the countries
 * 3. Emit the cached countries to UI
 */
class GetCountries(
    private val cache: CountryCache,
    private val service: CountryService,
) {

    /**
     * Withdraws data from the service and caches the result
     *
     * @return flow Flow<DataState<List<Country>>>
     */
    fun execute(): Flow<DataState<List<Country>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            // Uncomment here if you first want to output values in cache
            cache.selectAll().takeIf { it.isNotEmpty() }?.also { cachedCountries ->
                emit(DataState.Data(cachedCountries))
            }

            val countries: List<Country> = try { // catch network exceptions
                service.getAllCountries()
            }catch (e: Exception){
                e.printStackTrace() // log to crashlytics?
                emit(DataState.Response<List<Country>>(
                    uiComponent = UIComponent.Dialog(
                        title = "Network Data Error",
                        description = e.message?: "Unknown error"
                    )
                ))
                listOf()
            }

            // cache the network data
            cache.insert(countries)

            // emit data from cache
            val cachedCountries = cache.selectAll()

            emit(DataState.Data(cachedCountries))

        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Response<List<Country>>(
                uiComponent = UIComponent.Dialog(
                    title = "Error",
                    description = e.message?: "Unknown error"
                )
            ))
        }
        finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}




