package com.iliadmastery.ui_countrylist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.iliadmastery.components.DefaultScreenUI
import com.iliadmastery.core.domain.UIComponentState
import com.iliadmastery.ui_countrylist.components.CountryListFilter
import com.iliadmastery.ui_countrylist.components.CountryListItem
import com.iliadmastery.ui_countrylist.components.CountryListToolbar

/**
 * Renders list item countries as a RecyclerView
 *
 * @param state ui state to render
 * @param events callback to generate an input
 * @param navigateToDetailScreen callback on the selected country
 * @param imageLoader
 */
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun CountryList(
    state: CountryListState,
    events: (CountryListEvents) -> Unit,
    navigateToDetailScreen: (Long) -> Unit,
    imageLoader: ImageLoader,
) {
    DefaultScreenUI(
        queue = state.errorQueue,
        onRemoveHeadFromQueue = {
            events(CountryListEvents.OnRemoveHeadFromQueue)
        },
        progressBarState = state.progressBarState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        events(CountryListEvents.UpdateFilterDialogState(UIComponentState.Hide))
                    }
            ) {
                CountryListToolbar(
                    countryName = state.countryName,
                    onCountryNameChanged = { countryName ->
                        events(CountryListEvents.UpdateCountryName(countryName))
                    },
                    onExecuteSearch = {
                        events(CountryListEvents.FilterCountries)
                    },
                    onShowFilterDialog = {
                        events(CountryListEvents.UpdateFilterDialogState(UIComponentState.Show))
                    }
                )
                AnimatedVisibility(visible = state.filteredCountries.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        items(state.filteredCountries) { country ->
                            CountryListItem(
                                country = country,
                                onSelectCountry = navigateToDetailScreen,
                                imageLoader = imageLoader,
                            )
                        }
                    }
                }
            }
            if (state.filterDialogState is UIComponentState.Show) {
                CountryListFilter(
                    countryFilter = state.countryFilter,
                    onUpdateCountryFilter = { countryFilter ->
                        events(CountryListEvents.UpdateCountryFilter(countryFilter))
                    },
                    onCloseDialog = {
                        events(CountryListEvents.UpdateFilterDialogState(UIComponentState.Hide))
                    },
                    continentFilter = state.contientFilter,
                    onUpdateContinentFilter = { continent ->
                        events(CountryListEvents.UpdateContinentFilter(continent))
                    },

                    languageFilter = state.languageFilter,
                    languagesFilterable = state.languagesFilterable,
                    onUpdateLanguageFilter = { language ->
                        events(CountryListEvents.UpdateLanguageFilter(language))
                    },
                )
            }
        }
    }
}








