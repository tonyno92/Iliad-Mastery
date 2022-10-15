package com.iliadmastery.ui_countrylist.components.filters

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iliadmastery.components.DropDownList
import com.iliadmastery.country_domain.LanguageFilter
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_LANGUAGE_BUTTON
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN_ITEM
import com.iliadmastery.ui_countrylist.R

/**
 * Renders the dropdown of the language filter
 *
 * @param language [LanguageFilter]
 * @param languageItems List<[LanguageFilter.Language]>,
 * @param updateLanguageFilter ([LanguageFilter]) -> Unit,
 * @param removeFilterOnLanguage () -> Unit
 */
@ExperimentalAnimationApi
@Composable
fun LanguageFilterSelector(
    language: LanguageFilter,
    languageItems: List<LanguageFilter.Language>,
    updateLanguageFilter: (LanguageFilter) -> Unit,
    removeFilterOnLanguage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.dialog_filter_language),
                style = MaterialTheme.typography.h3,
            )
        }
    }

    val isOpen = remember { mutableStateOf(false) } // initial value
    val mapper: (String) -> LanguageFilter = { selected ->
        languageItems.find { it.uiValue == selected } ?: LanguageFilter.None
    }
    //Box {
    Column {
        OutlinedButton(
            onClick = { isOpen.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    text = language.uiValue,
                    color = MaterialTheme.colors.primary
                )

                val icon =
                    if (isOpen.value) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { isOpen.value = !isOpen.value },
                    imageVector = icon,
                    contentDescription = icon.name
                )
            }
        }
        DropDownList(
            modifier = Modifier
                .wrapContentWidth()
                .testTag(TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN),
            modifierItems = Modifier
                .fillMaxWidth()
                .testTag(TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN_ITEM),
            isOpen = isOpen.value,
            items = languageItems.map { it.uiValue },
            none = LanguageFilter.None.uiValue,
            mapper = mapper,
            onToggled = { isOpen.value = it },
            onSelected = updateLanguageFilter,
            onRemoved = removeFilterOnLanguage
        )
    }

    //}
}