package com.iliadmastery.country_datasource_test.network

import com.iliadmastery.country_datasource.network.model.CountryDto
import com.iliadmastery.country_datasource.network.model.toCountry
import com.iliadmastery.country_domain.Country
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

/**
 * serialize mocked json data to Business model
 *
 * @param jsonData
 * @return List[Country]
 */
fun serializeCountryData(jsonData: String): List<Country> {
    return json.decodeFromString<List<CountryDto>>(jsonData).map { it.toCountry() }
}