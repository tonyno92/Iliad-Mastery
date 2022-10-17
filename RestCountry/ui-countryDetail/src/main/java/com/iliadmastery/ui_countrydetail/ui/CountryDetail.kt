package com.iliadmastery.ui_countrydetail.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.iliadmastery.components.DefaultScreenUI
import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_domain.CountryAssignedStatusEnum
import com.iliadmastery.ui_countrydetail.R

/**
 * Composable function that make a [com.iliadmastery.demo.ui.navigation.Screen.CountryDetail]
 *
 * @param state ui state to render
 * @param events callback to generate an input
 * @param imageLoader coil image loader
 */
@ExperimentalAnimationApi
@Composable
fun CountryDetail(
    state: CountryDetailState,
    events: (CountryDetailEvents) -> Unit,
    imageLoader: ImageLoader,
    onBackNavigation: () -> Unit,
) {
    DefaultScreenUI(
        queue = state.errorQueue,
        onRemoveHeadFromQueue = {
            events(CountryDetailEvents.OnRemoveHeadFromQueue)
        },
        progressBarState = state.progressBarState,
    ) {
        Column {
            TopAppBar(
                title = { Text(text = state.country?.name?:"") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onBackNavigation() },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            )
            state.country?.let { country ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background)
                        ) {
                            val painter = rememberImagePainter(country.flag,
                                imageLoader = imageLoader,
                                builder = {
                                    placeholder(if (isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background)
                                })
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 200.dp),
                                painter = painter,
                                contentDescription = country.name,
                                contentScale = ContentScale.Crop,
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .weight(weight = .8f, fill = true)
                                            .align(Alignment.CenterVertically)
                                            .padding(end = 8.dp),
                                        text = country.name,
                                        style = MaterialTheme.typography.h1,
                                    )
                                    val iconPainter = rememberImagePainter(country.coatOfArms,
                                        imageLoader = imageLoader,
                                        builder = {
                                            placeholder(if (isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background)
                                        })
                                    Image(
                                        modifier = Modifier
                                            .weight(weight = .2f, fill = false)
                                            .height(60.dp)
                                            //.width(30.dp)
                                            .align(Alignment.CenterVertically),
                                        painter = iconPainter,
                                        contentDescription = country.name,
                                        contentScale = ContentScale.Fit,
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    text = stringResource(id = R.string.country_detail_continents) + country.continents.joinToString(),
                                    style = MaterialTheme.typography.subtitle1,
                                )
                                Text(
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    text = stringResource(id = R.string.country_detail_languages) + country.languages.joinToString(),
                                    style = MaterialTheme.typography.caption,
                                )
                                CountryStatisticalStats(country = country, padding = 10.dp)
                                Spacer(modifier = Modifier.height(12.dp))
                                CountryStats(country = country)
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Displays Statistical Country info
 *
 * @param country [Country]
 * @param padding [Dp]
 */
@Composable
fun CountryStatisticalStats(
    country: Country,
    padding: Dp,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(), elevation = 8.dp, shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                text = stringResource(id = R.string.country_detail_stats),
                style = MaterialTheme.typography.h4,
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 20.dp)
                ) {
                    Row( // Area
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${stringResource(R.string.country_detail_area)}:",
                            style = MaterialTheme.typography.body2,
                        )
                        val area = remember { country.area }
                        Row {
                            Text(
                                text = "$area km²",
                                style = MaterialTheme.typography.body2,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row( // Population
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "${stringResource(R.string.country_detail_population)}: ",
                            style = MaterialTheme.typography.body2,
                        )
                        val numOfResidents = remember { country.population }
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "$numOfResidents ab.",
                            style = MaterialTheme.typography.body2,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )

                {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.country_detail_add_more)
                    )
                }
            }
        }

    }
}

/**
 * Displays most important Country info
 *
 * @param country [Country]
 */
@Composable
fun CountryStats(
    country: Country,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Assigned Status
        Column(
            modifier = Modifier.fillMaxWidth(.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.country_detail_assigned_status),
                style = MaterialTheme.typography.h2,
            )

            val assignedStatus = country.status
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
                text = assignedStatus.name.lowercase(),
                style = MaterialTheme.typography.h2,
                color = if (assignedStatus == CountryAssignedStatusEnum.OFFICIALLY) Color(0xFF009a34) else MaterialTheme.colors.error
            )
        }
        // UnMember
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.country_detail_member),
                style = MaterialTheme.typography.h2,
            )

            val isUnMember = country.unMember
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = if (isUnMember) R.string.country_detail_yes else R.string.country_detail_no),
                style = MaterialTheme.typography.h2,
                color = if (isUnMember) Color(0xFF009a34) else MaterialTheme.colors.error
            )
        }
    }
}













