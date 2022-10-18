package com.iliadmastery.demo.ui.endToEnd

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import com.iliadmastery.core.domain.Filter
import com.iliadmastery.country_datasource.cache.CountryCache
import com.iliadmastery.country_datasource.network.CountryService
import com.iliadmastery.country_datasource_test.cache.CountryCacheFake
import com.iliadmastery.country_datasource_test.cache.CountryDatabaseFake
import com.iliadmastery.country_datasource_test.network.CountryServiceFake
import com.iliadmastery.country_datasource_test.network.CountryServiceResponseType
import com.iliadmastery.country_domain.ContinentFilter
import com.iliadmastery.country_domain.LanguageFilter
import com.iliadmastery.country_interactors.*
import com.iliadmastery.demo.assertOnEqualScreenshot
import com.iliadmastery.demo.coil.FakeImageLoader
import com.iliadmastery.demo.di.InteractorsModule
import com.iliadmastery.demo.ui.MainActivity
import com.iliadmastery.demo.ui.ComposeTestFacade
import com.iliadmastery.demo.ui.navigation.Screen
import com.iliadmastery.demo.ui.theme.RestCountryInfoTheme
import com.iliadmastery.ui_countrydetail.ui.CountryDetail
import com.iliadmastery.ui_countrydetail.ui.CountryDetailViewModel
import com.iliadmastery.ui_countrylist.ui.CountryList
import com.iliadmastery.ui_countrylist.ui.CountryListViewModel
import com.iliadmastery.ui_countrylist.ui.test.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton


/**
 *
 * End to end tests for the CountryList Screen.
 * Basically I tested all the things a user could do in this screen.
 * 1. Searching for a country by name
 * 2. Ordering the data by country name (desc and asc)
 * 3. Filtering the data by continent
 * 4. Filtering the data by language
 */
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@UninstallModules(InteractorsModule::class)
@HiltAndroidTest
class CountryListEndToEnd {

    @Module
    @InstallIn(SingletonComponent::class)
    object TestCountryInteractorsModule {

        @Provides
        @Singleton
        fun provideCountryCache(): CountryCache {
            return CountryCacheFake(CountryDatabaseFake())
        }

        @Provides
        @Singleton
        fun provideCountryService(): CountryService {
            return CountryServiceFake.build(
                type = CountryServiceResponseType.CorrectData
            )
        }

        @Provides
        @Singleton
        fun provideCountryInteractors(
            cache: CountryCache, service: CountryService
        ): CountryInteractors {
            return CountryInteractors(
                getCountries = GetCountries(
                    cache = cache,
                    service = service,
                ),
                filterCountries = FilterCountries(),
                getCountryFromCache = GetCountryFromCache(
                    cache = cache,
                ),
                getLanguageFilterable = GetLanguageFilterable(
                    cache = cache
                )
            )
        }
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)

    @Before
    fun before() {
        composeTestRule.activity.setContent {
            RestCountryInfoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = Screen.CountryList.route, builder = {
                        composable(
                            route = Screen.CountryList.route,
                        ) {
                            val viewModel: CountryListViewModel = hiltViewModel()
                            CountryList(
                                state = viewModel.state.value,
                                events = viewModel::onTriggerEvent,
                                navigateToDetailScreen = { countryId ->
                                    navController.navigate("${Screen.CountryDetail.route}/$countryId")
                                },
                                imageLoader = imageLoader,
                            )
                        }
                        composable(
                            route = Screen.CountryDetail.route + "/{countryId}",
                            arguments = Screen.CountryDetail.arguments,
                        ) {
                            val viewModel: CountryDetailViewModel = hiltViewModel()
                            CountryDetail(
                                state = viewModel.state.value,
                                events = viewModel::onTriggerEvent,
                                imageLoader = imageLoader,
                                onBackNavigation = {}
                            )
                        }
                    }
                )
            }
        }
    }

    @Test
    fun testShowCountryListUiTree() {
        composeTestRule.onRoot(useUnmergedTree = true)
            .printToLog("TAG")
    }

    @Test
    fun testSearchCountryByName() {
        val testName = "testSearchCountryByName"

        composeTestRule.apply {
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .onFirst()
                .assertIsDisplayed()
            assertOnEqualScreenshot(testName, "1", onRoot())

            // Search by text "Vatican City State"
            ComposeTestFacade.inputText(this, "Vatican City State", TAG_COUNTRY_SEARCH_BAR)
            // Confirms that only countries with similar names are shown
            onNodeWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .assertTextEquals("Vatican City State")
                .assertIsDisplayed()
            assertOnEqualScreenshot(testName, "2", onRoot())
            ComposeTestFacade.clearText(this, onTag = TAG_COUNTRY_SEARCH_BAR)


            // Search by text "Vatican City State"
            ComposeTestFacade.inputText(this, "Virgin Islands", TAG_COUNTRY_SEARCH_BAR)
            // Confirms that only countries with similar names are shown
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true).assertAll(
                hasText("Virgin Islands of the United States").or(hasText("Virgin Islands"))
            )
            assertOnEqualScreenshot(testName, "3", onRoot())
            ComposeTestFacade.clearText(this, onTag = TAG_COUNTRY_SEARCH_BAR)


            // Search by text "Vatican City State"
            ComposeTestFacade.inputText(this, "Ita", TAG_COUNTRY_SEARCH_BAR)
            // Confirms that only countries with similar names are shown
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true).assertAll(
                hasText("Italian Republic", ignoreCase = true)
                    .or(
                        hasText("Great Britain", substring = true, ignoreCase = true)
                    ).or(
                        hasText("islamic republic of mauritania", ignoreCase = true)
                    )
            )
            assertOnEqualScreenshot(testName, "4", onRoot())
            ComposeTestFacade.clearText(this, onTag = TAG_COUNTRY_SEARCH_BAR)
        }
    }

    @Test
    fun testFilterCountryAlphabetically() {
        val testName = "testFilterCountryAlphabetically"

        composeTestRule.apply {
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .onFirst()
                .assertIsDisplayed()
            assertOnEqualScreenshot(testName, "1", onRoot())

            // Show the dialog
            macroOpenFilterDialog()


            // Filter by "Country" name
            onNodeWithTag(TAG_COUNTRY_FILTER_COUNTRY_CHECKBOX).performClick()

            // Order Descending (z-a)
            onNodeWithTag(TAG_COUNTRY_FILTER_DESC).performClick()
            assertOnEqualScreenshot(testName, "2", onNode(isDialog()))

            // Close the dialog
            macroCloseFilterDialog()

            // Confirm the order is correct
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .assertAny(hasText("Virgin Islands"))
            assertOnEqualScreenshot(testName, "3", onRoot())

            // Show the dialog
            macroOpenFilterDialog()
            assertOnEqualScreenshot(testName, "4", onNode(isDialog()))

            // Order Ascending (a-z)

            onNodeWithTag(TAG_COUNTRY_FILTER_ASC).performClick()
            assertOnEqualScreenshot(testName, "5", onNode(isDialog()))

            // Close the dialog
            macroCloseFilterDialog()

            // Confirm the order is correct
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .assertAny(hasText("American Samoa"))
            assertOnEqualScreenshot(testName, "6", onRoot())

        }
    }

    @Test
    fun testFilterCountryByContinent() {
        val testName = "testFilterCountryByContinent"

        composeTestRule.apply {
            // Choose a random continent from those available
            ContinentFilter.Oceania.also { oceania ->
                onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                    .onFirst()
                    .assertIsDisplayed()
                assertOnEqualScreenshot(testName, "1", onRoot())

                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(testName, "2", onNode(isDialog()))

                // Select the continent
                macroSelectFilter(oceania, TAG_COUNTRY_FILTER_CONTINENT_RADIOBUTTON)
                assertOnEqualScreenshot(testName, "3", onNode(isDialog()))

                // Close the dialog
                macroCloseFilterDialog()

                // Confirm that only [continent] countries are showing
                onAllNodesWithTag(TAG_COUNTRY_CONTINENTS, useUnmergedTree = true)
                    .assertAll(hasText(oceania.uiValue))
                assertOnEqualScreenshot(testName, "4", onRoot())

            }
        }
    }

    @Test
    fun testFilterCountryByEachContinent() {
        val testName = "testFilterCountryByEachContinent"

        composeTestRule.apply {
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .onFirst()
                .assertIsDisplayed()
            assertOnEqualScreenshot(testName, "1", onRoot())

            // Choose a random continent from those available
            ContinentFilter.allContinents.onEach { continent ->

                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(
                    testName,
                    "${continent.uiValue}_1",
                    onNode(isDialog())
                )

                macroSelectFilter(continent, TAG_COUNTRY_FILTER_CONTINENT_RADIOBUTTON)
                assertOnEqualScreenshot(
                    testName,
                    "${continent.uiValue}_2",
                    onNode(isDialog())
                )

                // Close the dialog
                macroCloseFilterDialog()

                // Confirm that only country with [continent] care showing
                onAllNodesWithTag(TAG_COUNTRY_CONTINENTS, useUnmergedTree = true)
                    .assertAll(hasText(continent.uiValue))
                assertOnEqualScreenshot(testName, "${continent.uiValue}_3", onRoot())

            }
        }
    }

    @Test
    fun testLanguageFilterCountryDropDown() {
        val testName = "testLanguageFilterCountryDropDown"
        composeTestRule.apply {

            LanguageFilter.Language("Italian").also { language: Filter ->
                onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                    .onFirst()
                    .assertIsDisplayed()
                assertOnEqualScreenshot(testName, "1", onRoot())

                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(testName, "2", onNode(isDialog()))

                // Open language dropdown
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON).performClick()

                // Verify that dropdown is showing
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN)
                    .assertIsDisplayed()

                macroSelectFilter(
                    language,
                    TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN_ITEM
                )

                // Confirm that the language filter are selects
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON)
                    .assertTextEquals(language.uiValue)
                assertOnEqualScreenshot(testName, "3", onNode(isDialog()))
            }
        }
    }

    @Test
    fun testFilterCountryByLanguage() {
        val testName = "testFilterCountryByLanguage"

        composeTestRule.apply {

            LanguageFilter.Language("Italian").also { language: Filter ->
                onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                    .onFirst()
                    .assertIsDisplayed()
                assertOnEqualScreenshot(testName, "1", onRoot())

                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(testName, "2", onNode(isDialog()))

                // Show the drop down languages
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON)
                    .assertIsDisplayed()
                    .performClick()

                // Confirm the filter drop down language is showing
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN)
                    .assertIsDisplayed()

                // Select language from drop down
                macroSelectFilter(
                    language,
                    TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN_ITEM
                )

                // Confirm the filter dialog is showing
                onNodeWithTag(TAG_COUNTRY_FILTER_DIALOG).assertIsDisplayed()

                // Check that the correct language was select
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON)
                    .assertIsDisplayed()
                    .assertTextEquals(language.uiValue)
                assertOnEqualScreenshot(testName, "3", onNode(isDialog()))

                // Close the dialog
                macroCloseFilterDialog()

                // Confirm that only country with [language] care showing
                onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = false)
                    .assertAll(
                        hasText("Vatican City State")
                            .or(hasText("Swiss Confederation"))
                            .or(hasText("Republic of San Marino"))
                            .or(hasText("Italian Republic"))
                    )
                assertOnEqualScreenshot(testName, "4", onRoot())
            }
        }
    }

    @Test
    fun testFilterCountryResetAll() {
        val testName = "testFilterCountryResetAll"

        composeTestRule.apply {
            onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = true)
                .onFirst()
                .assertIsDisplayed()
            assertOnEqualScreenshot(testName, "1", onRoot())

            // Choose a random continent from those available
            ContinentFilter.Europe.also { continent ->
                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(testName, "2", onNode(isDialog()))

                // Select the continent
                macroSelectFilter(
                    continent,
                    TAG_COUNTRY_FILTER_CONTINENT_RADIOBUTTON
                )
                assertOnEqualScreenshot(testName, "3", onNode(isDialog()))

                // Close the dialog
                macroCloseFilterDialog()

                // Confirm that only [continent] countries are showing
                onAllNodesWithTag(TAG_COUNTRY_CONTINENTS, useUnmergedTree = true)
                    .assertAll(hasText(continent.uiValue))
                assertOnEqualScreenshot(testName, "4", onRoot())
            }


            LanguageFilter.Language("Italian").also { language: Filter ->

                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(testName, "5", onNode(isDialog()))

                // Show the drop down languages
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON)
                    .assertIsDisplayed()
                    .performClick()

                // Confirm the filter drop down language is showing
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN)
                    .assertIsDisplayed()

                // Select language from drop down
                macroSelectFilter(language, TAG_COUNTRY_FILTER_LANGUAGE_DROP_DOWN_ITEM)

                // Check that the correct language was select
                onNodeWithTag(TAG_COUNTRY_FILTER_LANGUAGE_BUTTON)
                    .assertIsDisplayed()
                    .assertTextEquals(language.uiValue)
                assertOnEqualScreenshot(testName, "6", onNode(isDialog()))


                // Confirm the filter dialog is showing
                onNodeWithTag(TAG_COUNTRY_FILTER_DIALOG).assertIsDisplayed()

                // Close the dialog
                macroCloseFilterDialog()


                onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = false)
                    .assertAll(
                        hasText("Vatican City State")
                            .or(hasText("Swiss Confederation"))
                            .or(hasText("Republic of San Marino"))
                            .or(hasText("Italian Republic"))
                    )
                assertOnEqualScreenshot(testName, "7", onRoot())

                // Show the dialog
                macroOpenFilterDialog()
                assertOnEqualScreenshot(testName, "8", onNode(isDialog()))

                // Reset all filter
                macroResetFilterDialog()

                // Close the dialog
                macroCloseFilterDialog()

                onAllNodesWithTag(TAG_COUNTRY_NAME, useUnmergedTree = false)
                    .assertAll(
                        //hasText("Vatican City State")
                        hasText("United States of America")
                            .or(hasText("Virgin Islands"))
                            .or(hasText("Åland Islands"))
                    )
                assertOnEqualScreenshot(testName, "9", onRoot())
            }
        }
    }


    /**
     * Screen Macros
     */

    private fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.macroSelectFilter(
        filter: Filter,
        filterTag: String
    ) {
        ComposeTestFacade.select(
            this,
            filter.uiValue,
            filterTag
        )
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.macroOpenFilterDialog() {
        ComposeTestFacade.openDialog(
            this,
            TAG_COUNTRY_FILTER_DIALOG,
            TAG_COUNTRY_FILTER_BTN
        )
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.macroCloseFilterDialog() {
        ComposeTestFacade.closeDialog(
            composeTestRule,
            TAG_COUNTRY_FILTER_DIALOG,
            TAG_COUNTRY_FILTER_DIALOG_DONE
        )
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.macroResetFilterDialog() {
        // Reset all filter
        onNodeWithTag(TAG_COUNTRY_FILTER_DIALOG_RESET).performClick()
        // Confirm that filter dialog is showing
        onNodeWithTag(TAG_COUNTRY_FILTER_DIALOG).assertIsDisplayed()
    }
}




















