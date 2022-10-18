package com.iliadmastery.ui_countrylist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.iliadmastery.country_domain.Country
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_NAME
import com.iliadmastery.ui_countrylist.ui.test.TAG_COUNTRY_CONTINENTS
import com.iliadmastery.ui_countrylist.R

/**
 * Renders single country item
 *
 * @param country
 * @param onSelectCountry
 * @param imageLoader
 */
@ExperimentalAnimationApi
@Composable
fun CountryListItem(
    country: Country,
    onSelectCountry: (Int) -> Unit,
    imageLoader: ImageLoader,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colors.surface)
            .clickable {
                onSelectCountry(country.id)
            },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /**
             * Recommended use!!
             * This is the first way to load a background image
             * The second way is using a painter, but it doesn't recommended.
             */
            AsyncImage(
                modifier = Modifier
                    .width(120.dp)
                    .height(70.dp),
                model = country.flag,
                contentDescription = "Country Flag",
                imageLoader = imageLoader,
                placeholder = painterResource(if (isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(.8f) // fill 80% of remaining width
                    .padding(start = 12.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .testTag(TAG_COUNTRY_NAME),
                    text = country.name,
                    style = MaterialTheme.typography.h4,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .testTag(TAG_COUNTRY_CONTINENTS),
                    text = country.continents.joinToString(),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}
















