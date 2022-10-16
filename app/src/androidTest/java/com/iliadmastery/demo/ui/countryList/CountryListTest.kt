package com.iliadmastery.demo.ui.countryList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import com.iliadmastery.demo.coil.FakeImageLoader
import com.iliadmastery.demo.ui.theme.RestCountryInfoTheme
import com.iliadmastery.country_datasource_test.network.serializeCountryData
import com.iliadmastery.ui_countrylist.ui.CountryList
import com.iliadmastery.ui_countrylist.ui.CountryListState
import org.junit.Rule
import org.junit.Test

/**
 * Demo isolation test for CountryList screen.
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class CountryListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)
    private val countryData = serializeCountryData(CountryDataValid.data)


    @Test
    fun areCountriesShown() {
        composeTestRule.apply {

            setContent {
                RestCountryInfoTheme {
                    val state = remember {
                        CountryListState(
                            countries = countryData,
                            filteredCountries = countryData,
                        )
                    }
                    CountryList(
                        state = state,
                        events = {},
                        navigateToDetailScreen = {},
                        imageLoader = imageLoader,
                    )
                }
            }

            onRoot(useUnmergedTree = true).printToLog("TAG")
            //composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.ContentDescription, listOf("Kingdom of Spain"))).assertIsDisplayed()

            onNodeWithContentDescription("Iceland").assertIsDisplayed()
            onNodeWithText("Iceland").assertIsDisplayed()
            onNodeWithText("Kingdom of Spain").assertIsDisplayed()
            onNodeWithText("Republic of Iraq").assertIsDisplayed()
        }
    }

}














