package com.iliadmastery.country_interactors

import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_datasource.cache.CountryCache
import com.iliadmastery.country_domain.LanguageFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Rappresenting a Use-Case
 *
 * 1. Retrieve the countries from cache
 * 2. Calculate all languages distinctly by countries
 * 3. Emit the languages filterable to UI
 */
class GetLanguageFilterable(
    private val cache: CountryCache
) {

    fun execute(): Flow<DataState<List<LanguageFilter.Language>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            // retrive data from cache
            val cachedCountries = cache.selectAll()

            if(cachedCountries.isEmpty()){
                throw Exception("There aren't countries into cache.")
            }

            val languages =
                cachedCountries
                    .flatMap { it.languages }
                    .filterNot { it.isBlank() }
                    .toSet()
                    .map { LanguageFilter.Language(it) }
                    .sortedBy { it.uiValue }

            emit(DataState.Data(languages))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                DataState.Response<List<LanguageFilter.Language>>(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.message ?: "Unknown error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }

}




