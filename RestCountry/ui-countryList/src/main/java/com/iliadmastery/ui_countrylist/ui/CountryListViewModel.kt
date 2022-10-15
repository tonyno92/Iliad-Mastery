package com.iliadmastery.ui_countrylist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.Queue
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.core.util.Logger
import com.iliadmastery.country_domain.ContinentFilter
import com.iliadmastery.country_domain.CountryFilter
import com.iliadmastery.country_domain.LanguageFilter
import com.iliadmastery.country_interactors.FilterCountries
import com.iliadmastery.country_interactors.GetCountries
import com.iliadmastery.country_interactors.GetLanguageFilterable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel
@Inject
constructor(
    private val getCountries: GetCountries,
    private val filterCountries: FilterCountries,
    private val getLanguageFilterable: GetLanguageFilterable,
    private val logger: Logger,
): ViewModel(){

    // Ui state
    val state: MutableState<CountryListState> = mutableStateOf(CountryListState())

    init {
        onTriggerEvent(CountryListEvents.GetCountries)
    }

    /**
     * Function exposed to the view to generate events
     *
     * @param event : [CountryListEvents]
     */
    fun onTriggerEvent(event: CountryListEvents){
        when(event){
            is CountryListEvents.GetCountries -> {
                getCountries()
            }
            is CountryListEvents.FilterCountries -> {
                filterCountries()
            }
            is CountryListEvents.UpdateCountryFilter -> {
                updateCountryFilter(event.countryFilter)
            }
            is CountryListEvents.UpdateContinentFilter -> {
               updateContinentFilter(event.continent)
            }
            is CountryListEvents.UpdateLanguageFilter -> {
                updateLanguageFilter(event.language)
            }
            is CountryListEvents.UpdateCountryName -> {
                updateCountryName(event.countryName)
            }
            is CountryListEvents.UpdateFilterDialogState -> {
                state.value = state.value.copy(filterDialogState = event.uiComponentState)
            }
            is CountryListEvents.OnRemoveHeadFromQueue -> {
                removeHeadMessage()
            }
            is CountryListEvents.Error -> {
                if(event.uiComponent is UIComponent.None){
                    logger.log("getCountries: ${event.uiComponent.message}")
                }
                else{
                    appendToMessageQueue(event.uiComponent)
                }
            }
        }
    }

    /**
     * Update the state with new search typed by the user
     *
     * @param countryName
     */
    private fun updateCountryName(countryName: String) {
        state.value = state.value.copy(countryName = countryName)
    }

    /**
     * Update the state with new [LanguageFilter] selected by the user
     *
     * @param languageFilter
     */
    private fun updateLanguageFilter(languageFilter: LanguageFilter){
        state.value = state.value.copy(languageFilter = languageFilter)
        filterCountries()
    }

    /**
     * Update the state with new [ContinentFilter] selected by the user
     *
     * @param contientFilter
     */
    private fun updateContinentFilter(contientFilter: ContinentFilter){
        state.value = state.value.copy(contientFilter = contientFilter)
        filterCountries()
    }

    /**
     * Update the state with new [CountryFilter] selected by the user
     *
     * @param countryFilter
     */
    private fun updateCountryFilter(countryFilter: CountryFilter){
        state.value = state.value.copy(countryFilter = countryFilter)
        filterCountries()
    }

    /**
     * Filters country by status of all filters
     */
    private fun filterCountries(){
        val filteredList = filterCountries.execute(
            current = state.value.countries,
            countryName = state.value.countryName,
            countryFilter = state.value.countryFilter,
            continentFilter = state.value.contientFilter,
            languageFilter = state.value.languageFilter
        )
        state.value = state.value.copy(filteredCountries = filteredList)
    }

    /**
     * Retrives the country list and cache the result
     */
    private fun getCountries(){
        getCountries.execute().onEach { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(countries = dataState.data?: listOf())
                    getLanguageFilterable()
                    filterCountries()

                }
                is DataState.Response -> {
                    if(dataState.uiComponent is UIComponent.None){
                        logger.log("getCountries: ${(dataState.uiComponent as UIComponent.None).message}")
                    }
                    else{
                        appendToMessageQueue(dataState.uiComponent)
                    }
                }
            }
        }
            .launchIn(viewModelScope)
    }

    /**
     * Calculate all distinct languages from list of countries
     */
    private fun getLanguageFilterable(){
        getLanguageFilterable.execute().onEach { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(languagesFilterable = dataState.data ?: emptyList())
                    filterCountries()
                }
                is DataState.Response -> {
                    if(dataState.uiComponent is UIComponent.None){
                        logger.log("getLanguageFilterable: ${(dataState.uiComponent as UIComponent.None).message}")
                    }
                    else{
                        appendToMessageQueue(dataState.uiComponent)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent){
        val queue = state.value.errorQueue
        queue.add(uiComponent)
        state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        state.value = state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = state.value.errorQueue
            queue.remove() // can throw exception if empty
            state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            state.value = state.value.copy(errorQueue = queue)
        }catch (e: Exception){
            logger.log("Nothing to remove from DialogQueue")
        }
    }
}












