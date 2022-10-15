package com.iliadmastery.ui_countrylist.components.filters

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iliadmastery.country_domain.ContinentFilter
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_CONTINENT_RADIOBUTTON
import com.iliadmastery.ui_countrylist.R

/**
 * Renders container of continent filter
 *
 * @param continent [ContinentFilter] current continent selected
 * @param updateContinentFilter ([ContinentFilter]) -> Unit on selection callback
 * @param removeFilterOnContinent () -> Unit on remove filter callback
 */
@ExperimentalAnimationApi
@Composable
fun ContinentFilterSelector(
    continent: ContinentFilter,
    updateContinentFilter: (continent: ContinentFilter) -> Unit,
    removeFilterOnContinent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.dialog_filter_continent),
                style = MaterialTheme.typography.h3,
            )
        }

        ContinentSelector(
            selected = continent,
            onUpdateContinentFilter = { continent ->
                updateContinentFilter(continent)
            },
            onRemoveAttributeFilter = {
                removeFilterOnContinent()
            }
        )
    }
}

/**
 * Renders continent selector
 *
 * @param selected [ContinentFilter]
 * @param onUpdateContinentFilter ([ContinentFilter]) -> Unit
 * @param onRemoveAttributeFilter () -> Unit
 */
@Composable
fun ContinentSelector(
    selected: ContinentFilter,
    onUpdateContinentFilter: (continent: ContinentFilter) -> Unit,
    onRemoveAttributeFilter: () -> Unit
) {

    ContinentFilter.allContinents.onEach { filter ->
        ContinentItem(
            continent = filter,
            checked = filter == selected,
            onSelected = { continent -> onUpdateContinentFilter(continent) }
        )
    }

    ContinentItem(
        continent = ContinentFilter.None,
        checked = selected is ContinentFilter.None,
        onSelected = { onRemoveAttributeFilter() }
    )
}

/**
 * Renders a single continent filter item
 *
 * @param continent [ContinentFilter]
 * @param checked [Boolean]
 * @param onSelected ([ContinentFilter]) -> Unit
 */
@Composable
fun ContinentItem(
    continent: ContinentFilter,
    checked: Boolean,
    onSelected: (continent: ContinentFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 8.dp)
            .testTag(TAG_COUNTRY_FILTER_CONTINENT_RADIOBUTTON)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null, // disable the highlight
                onClick = {
                    onSelected(continent)
                },
            ),
    ) {
        RadioButton(
            selected = checked,
            onClick = { onSelected(continent) },
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
        )

        Text(
            text = continent.uiValue,
            style = MaterialTheme.typography.body1,
        )
    }
}