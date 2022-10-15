package com.iliadmastery.ui_countrylist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iliadmastery.components.EmptyRow
import com.iliadmastery.core.domain.FilterOrder
import com.iliadmastery.country_domain.ContinentFilter
import com.iliadmastery.country_domain.CountryFilter
import com.iliadmastery.country_domain.LanguageFilter
import com.iliadmastery.ui_countrylist.components.filters.ContinentFilterSelector
import com.iliadmastery.ui_countrylist.components.filters.CountryFilterSelector
import com.iliadmastery.ui_countrylist.components.filters.LanguageFilterSelector
import com.iliadmastery.ui_countrylist.ui.test.*


/**
 * Renders dialog filter
 *
 * @param countryFilter Sort filter enabled
 * @param onCloseDialog callback on tap on close
 * @param continentFilter continent filter active
 * @param languageFilter language filter active
 * @param languagesFilterable list of language filterable
 * @param onUpdateCountryFilter callback on change sort order
 * @param onUpdateContinentFilter callback on change continent filter
 * @param onUpdateLanguageFilter callback on change language filter
 */
@ExperimentalAnimationApi
@Composable
fun CountryListFilter(
    countryFilter: CountryFilter,
    onCloseDialog: () -> Unit,
    continentFilter: ContinentFilter = ContinentFilter.None,
    languageFilter: LanguageFilter = LanguageFilter.None,
    languagesFilterable: List<LanguageFilter.Language>,
    onUpdateCountryFilter: (CountryFilter) -> Unit,
    onUpdateContinentFilter: (ContinentFilter) -> Unit = {},
    onUpdateLanguageFilter: (LanguageFilter) -> Unit = {},
) {
    Dialog(onDismissRequest = onCloseDialog) {
        Surface(
            modifier = Modifier.testTag(TAG_COUNTRY_FILTER_DIALOG),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colors.surface
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    /**
                     * Header
                     */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter",
                            style = MaterialTheme.typography.h2,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .testTag(TAG_COUNTRY_FILTER_DIALOG_RESET)
                                    .clickable {
                                        onUpdateContinentFilter(ContinentFilter.None)
                                        onUpdateLanguageFilter(LanguageFilter.None)
                                    },
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = Color(0xFFE91E63)
                            )
                            Icon(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .testTag(TAG_COUNTRY_FILTER_DIALOG_DONE)
                                    .clickable {
                                        onCloseDialog()
                                    },
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Color(0xFF009a34)
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    /**
                     * Content
                     */
                    LazyColumn {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                // Spacer isn't working for some reason so use Row to create space
                                EmptyRow()

                                // Country Filter
                                CountryFilterSelector(
                                    filterOnCountry = {
                                        onUpdateCountryFilter(CountryFilter.Country())
                                    },
                                    isEnabled = countryFilter is CountryFilter.Country,
                                    order = if (countryFilter is CountryFilter.Country) countryFilter.order else null,
                                    orderDesc = {
                                        onUpdateCountryFilter(
                                            CountryFilter.Country(
                                                order = FilterOrder.Descending
                                            )
                                        )
                                    },
                                    orderAsc = {
                                        onUpdateCountryFilter(
                                            CountryFilter.Country(
                                                order = FilterOrder.Ascending
                                            )
                                        )
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(
                                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                    thickness = 1.dp
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                ContinentFilterSelector(
                                    continent = continentFilter,
                                    updateContinentFilter = { continent ->
                                        onUpdateContinentFilter(
                                            continent
                                        )
                                    },
                                    removeFilterOnContinent = {
                                        onUpdateContinentFilter(
                                            ContinentFilter.None
                                        )
                                    }
                                )

                                LanguageFilterSelector(
                                    language = languageFilter,
                                    languageItems = languagesFilterable,
                                    updateLanguageFilter = { language ->
                                        onUpdateLanguageFilter(
                                            language
                                        )
                                    },
                                    removeFilterOnLanguage = { onUpdateLanguageFilter(LanguageFilter.None) },
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

            }

        }
    }
}














