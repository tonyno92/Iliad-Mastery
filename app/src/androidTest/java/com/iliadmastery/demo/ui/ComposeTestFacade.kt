package com.iliadmastery.demo.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule

/**
 * AndroidComposeTestRule Facade to simplify the most common interaction and operations with it
 */
class ComposeTestFacade {

    companion object {

        fun inputText(
            compose: AndroidComposeTestRule<*, *>,
            text: String,
            onTag: String,
            clear: Boolean = false
        ) {
            // Perform text input\clear on specific tag
            compose.onNodeWithTag(onTag)
                .apply { if (clear) performTextClearance() else performTextInput(text) }
        }

        fun clearText(
            compose: AndroidComposeTestRule<*, *>,
            onTag: String
        ) {
            // Perform text clear on specific tag
            inputText(compose, text = "", onTag = onTag, clear = true)
        }

        fun select(
            compose: AndroidComposeTestRule<*, *>, itemToSelect: String, onTag: String
        ) {
            // Select filter on specific tag
            compose.onAllNodesWithTag(onTag)
                .filterToOne(hasText(itemToSelect))
                .performScrollTo()
                .performClick()
        }

        fun unSelectAll(
            compose: AndroidComposeTestRule<*, *>, onTag: String
        ) {
            compose.onNodeWithTag(onTag)
                .performClick()
        }

        fun openDialog(
            compose: AndroidComposeTestRule<*, *>, onDialogTag: String, onButtonTag: String
        ) {
            compose.apply {
                // Show the dialog
                onNodeWithTag(onButtonTag).performClick()

                // Confirm that dialog is showing
                onNodeWithTag(onDialogTag).assertIsDisplayed()
            }
        }

        fun closeDialog(
            compose: AndroidComposeTestRule<*, *>, onDialogTag: String, onCloseButtonTag: String
        ) {
            compose.apply {
                // Confirm that dialog is showing before close it
                onNodeWithTag(onDialogTag).assertIsDisplayed()

                // Close the dialog
                onNodeWithTag(onCloseButtonTag).performClick()

            }
        }
    }
}