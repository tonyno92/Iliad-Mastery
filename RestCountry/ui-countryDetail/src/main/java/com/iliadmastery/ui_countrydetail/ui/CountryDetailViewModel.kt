package com.iliadmastery.ui_countrydetail.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iliadmastery.core.domain.DataState
import com.iliadmastery.core.domain.Queue
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.core.util.Logger
import com.iliadmastery.country_interactors.GetCountryFromCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel
@Inject
constructor(
    private val getCountryFromCache: GetCountryFromCache,
    private val savedStateHandle: SavedStateHandle,
    private val logger: Logger,
): ViewModel(){

    // Ui state
    val state: MutableState<CountryDetailState> = mutableStateOf(CountryDetailState())

    init {
        savedStateHandle.get<Int>("countryId")?.let { countryId ->
            onTriggerEvent(CountryDetailEvents.GetCountryFromCache(countryId))
        }?: showError(
            uiComponent = UIComponent.Dialog(
                title = "Error",
                description = "Unable to retrieve the details for this country."
            )
        )
    }

    /**
     * Function exposed to the view to generate events
     *
     * @param event : [CountryDetailEvents]
     */
    fun onTriggerEvent(event: CountryDetailEvents){
        when(event){
            is CountryDetailEvents.GetCountryFromCache -> {
                getCountryFromCache(event.id)
            }
            is CountryDetailEvents.OnRemoveHeadFromQueue -> {
                removeHeadMessage()
            }
            is CountryDetailEvents.Error -> {
                if(event.uiComponent is UIComponent.None){
                    logger.log("getCountries: ${event.uiComponent.message}")
                }
                else{
                    appendToMessageQueue(event.uiComponent)
                }
            }
        }
    }

    private fun showError(uiComponent: UIComponent){
        onTriggerEvent(
            CountryDetailEvents.Error(
                uiComponent = uiComponent
            )
        )
    }

    private fun getCountryFromCache(id: Int){
        getCountryFromCache.execute(id).onEach { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(country = dataState.data)
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












