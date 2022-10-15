package com.iliadmastery.components

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * Create a generic dropdown menu for object of type [T]
 *
 * @param isOpen: current state of the drop-down menu
 * @param items: list of items to show
 * @param none: first element that cancels the filter
 * @param mapper: mapping function that transforms the value of the selected string into an object [T].
 * @param onToggled: function activated when opening \ closing the drop-down menu
 * @param onSelected: function activated when the element is selected
 * @param onRemoved: function activated when canceling the filter
 */
@Composable
fun <T> DropDownList(
    modifier: Modifier = Modifier,
    modifierItems: Modifier = Modifier,
    isOpen: Boolean = false,
    items: List<String>,
    none: String,
    mapper: (String) -> T,
    onToggled: (Boolean) -> Unit,
    onSelected: (T) -> Unit,
    onRemoved: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isOpen,
        onDismissRequest = { onToggled(false) },
    ) {
        DropdownMenuItem(
            modifier = modifierItems,
            onClick = {
                onRemoved()
                onToggled(false)
            }
        ) {
            Text(
                none,
                modifier = Modifier.wrapContentWidth(),
                style = MaterialTheme.typography.body1
            )
        }
        items.forEach {
            DropdownMenuItem(
                modifier = modifierItems,
                onClick = {
                    onSelected(mapper(it))
                    onToggled(false)
                }
            ) {
                Text(
                    it,
                    modifier = Modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}