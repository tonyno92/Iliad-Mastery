package com.iliadmastery.ui_countrylist.components.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iliadmastery.core.domain.FilterOrder
import com.iliadmastery.country_domain.CountryFilter
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_ASC
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_COUNTRY_CHECKBOX
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_DESC
import com.iliadmastery.ui_countrylist.R


/**
 * Renders the sort filter container
 *
 * @param filterOnCountry: Set the CountryFilter to 'Country'
 * @param isEnabled: Is the Country filter the selected 'CountryFilter'
 * @param order: Ascending or Descending?
 * @param orderDesc: Set the order to descending.
 * @param orderAsc: Set the order to ascending.
 */
@ExperimentalAnimationApi
@Composable
fun CountryFilterSelector(
    filterOnCountry: () -> Unit,
    isEnabled: Boolean,
    order: FilterOrder? = null,
    orderDesc: () -> Unit,
    orderAsc: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag(TAG_COUNTRY_FILTER_COUNTRY_CHECKBOX)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = true,
                    onClick = {
                        filterOnCountry()
                    },
                ),
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                checked = isEnabled,
                onCheckedChange = {
                    filterOnCountry()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = CountryFilter.Country().uiValue,
                style = MaterialTheme.typography.h3,
            )
        }

        OrderSelector(
            descString = stringResource(id = R.string.dialog_filter_country_ordering_desc),
            ascString = stringResource(id = R.string.dialog_filter_country_ordering_asc),
            isEnabled = isEnabled,
            isDescSelected = isEnabled && order is FilterOrder.Descending,
            isAscSelected = isEnabled && order is FilterOrder.Ascending,
            onUpdateCountryFilterDesc = {
                orderDesc()
            },
            onUpdateCountryFilterAsc = {
                orderAsc()
            },
        )
    }
}

/**
 * Renders ascending and descending items selector
 *
 * @param descString: String displayed in the "descending" checkbox
 * @param ascString: String displayed in the "ascending" checkbox
 * @param isEnabled: Is this CountryFilter currently the selected CountryFilter?
 * @param isDescSelected: Is the "descending" checkbox selected?
 * @param isAscSelected: Is the "ascending" checkbox selected?
 * @param onUpdateCountryFilterDesc: Set the filter to Descending.
 * @param onUpdateCountryFilterAsc: Set the filter to Ascending.
 */
@ExperimentalAnimationApi
@Composable
fun OrderSelector(
    descString: String,
    ascString: String,
    isEnabled: Boolean,
    isDescSelected: Boolean,
    isAscSelected: Boolean,
    onUpdateCountryFilterDesc: () -> Unit,
    onUpdateCountryFilterAsc: () -> Unit,
) {
    // Descending Order
    AnimatedVisibility(visible = isEnabled) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
                .testTag(TAG_COUNTRY_FILTER_DESC)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = isEnabled,
                    onClick = {
                        onUpdateCountryFilterDesc()
                    },
                ),
        ) {
            RadioButton(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                enabled = isEnabled,
                selected = isEnabled && isDescSelected,
                onClick = {
                    onUpdateCountryFilterDesc()
                },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
            )
            Text(
                text = descString,
                style = MaterialTheme.typography.body1,
            )
        }
    }

    // Ascending Order
    AnimatedVisibility(visible = isEnabled) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
                .testTag(TAG_COUNTRY_FILTER_ASC)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = isEnabled,
                    onClick = {
                        onUpdateCountryFilterAsc()
                    },
                ),
        ) {
            RadioButton(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                enabled = isEnabled,
                selected = isEnabled && isAscSelected,
                onClick = {
                    onUpdateCountryFilterAsc()
                },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
            )
            Text(
                text = ascString,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}