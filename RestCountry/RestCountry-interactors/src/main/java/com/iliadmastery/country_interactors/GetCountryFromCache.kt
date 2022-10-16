package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_datasource.cache.CountryCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Rappresenting a Use-Case
 * Retrieve a country from the cache using the country's unique id
 */
class GetCountryFromCache(
    private val cache: CountryCache
) {

    fun execute(
        id: Int,
    ): Flow<DataState<Country>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            // emit data from network
            val cachedCountry = cache.getCountry(id)

            if(cachedCountry == null){
                throw Exception("That country does not exist in the cache.")
            }

            emit(DataState.Data(cachedCountry))
        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Response<Country>(
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