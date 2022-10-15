package com.iliadmastery.demo.ui.countryDetail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import com.iliadmastery.demo.coil.FakeImageLoader
import com.iliadmastery.demo.ui.theme.RestCountryInfoTheme
import com.iliadmastery.country_datasource_test.network.serializeCountryData
import com.iliadmastery.ui_countrydetail.ui.CountryDetail
import com.iliadmastery.ui_countrydetail.ui.CountryDetailState
import com.iliadmastery.ui_countrydetail.R
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Demo isolation test for CountryDetail screen.
 */
@ExperimentalAnimationApi
class CountryDetailTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)
    private val countryData = serializeCountryData(CountryDataValid.data)

    @Test
    fun isCountryDataShown() {
        composeTestRule.apply {
            // choose a country at random
            val country = countryData[Random.nextInt(0, countryData.size - 1)]
            setContent {
                RestCountryInfoTheme() {
                    val state = remember {
                        CountryDetailState(
                            country = country,
                        )
                    }
                    CountryDetail(
                        state = state,
                        events = {},
                        imageLoader = imageLoader,
                    )
                }
            }
            onNodeWithText(country.name).assertIsDisplayed()

            val continents =
                context.getString(R.string.country_detail_continents) + country.continents.joinToString()
            onNodeWithText(continents).assertIsDisplayed()

            val languages =
                context.getString(R.string.country_detail_languages) + country.languages.joinToString()
            onNodeWithText(languages).assertIsDisplayed()

            val surface = "${country.area} km²"
            onNodeWithText(surface).assertIsDisplayed()

            val numOfResident = "${country.population} ab."
            onNodeWithText(numOfResident).assertIsDisplayed()

            val assignedStatus = country.status.name.lowercase()
            onNodeWithText(assignedStatus)

            val unMember = country.unMember
            context.apply {
                onNodeWithText(
                    getString(
                        if (unMember) R.string.country_detail_yes
                        else R.string.country_detail_no
                    )
                )
            }
        }

    }

}
















