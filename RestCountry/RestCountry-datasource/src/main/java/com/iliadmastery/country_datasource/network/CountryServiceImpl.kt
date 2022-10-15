package com.iliadmastery.country_datasource.network

import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_datasource.network.model.CountryDto
import com.iliadmastery.country_datasource.network.model.toCountry
import io.ktor.client.*
import io.ktor.client.request.*

class CountryServiceImpl(
    private val httpClient: HttpClient,
) : CountryService {

    override suspend fun getAllCountries(): List<Country> {
        return httpClient.get<List<CountryDto>> {
            url(EndPoints.COUNTRY_ALL)
        }.map { it.toCountry() }
    }
}