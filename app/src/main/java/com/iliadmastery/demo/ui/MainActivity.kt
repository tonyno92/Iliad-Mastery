package com.iliadmastery.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.iliadmastery.demo.ui.navigation.Screen
import com.iliadmastery.demo.ui.theme.RestCountryInfoTheme
import com.iliadmastery.ui_countrydetail.ui.CountryDetail
import com.iliadmastery.ui_countrydetail.ui.CountryDetailViewModel
import com.iliadmastery.ui_countrylist.ui.CountryList
import com.iliadmastery.ui_countrylist.ui.CountryListViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestCountryInfoTheme {
                BoxWithConstraints {
                    val navController = rememberAnimatedNavController()
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.CountryList.route,
                        builder = {
                            addCountryList(
                                navController = navController,
                                imageLoader = imageLoader,
                                width = constraints.maxWidth / 2,
                            )
                            addCountryDetail(
                                navController = navController,
                                imageLoader = imageLoader,
                                width = constraints.maxWidth / 2
                            )
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
fun NavGraphBuilder.addCountryList(
    navController: NavController,
    imageLoader: ImageLoader,
    width: Int,
) {
    composable(
        route = Screen.CountryList.route,
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
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
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addCountryDetail(
    navController: NavHostController,
    imageLoader: ImageLoader,
    width: Int,
) {
    composable(
        route = Screen.CountryDetail.route + "/{countryId}",
        arguments = Screen.CountryDetail.arguments,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        val viewModel: CountryDetailViewModel = hiltViewModel()
        CountryDetail(
            state = viewModel.state.value,
            events = viewModel::onTriggerEvent,
            onBackNavigation = { navController.navigateUp() },
            imageLoader = imageLoader
        )
    }
}