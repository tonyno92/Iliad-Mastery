package com.iliadmastery.country_datasource.network

import com.iliadmastery.country_domain.Country
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*



interface CountryService {

    /**
     * Interface service for get all countries
     *
     * @return List<[Country]>
     */
    suspend fun getAllCountries(): List<Country>

    /**
     * Service Factory CountryService
     */
    companion object Factory {
        fun build(): CountryService {
            return CountryServiceImpl(
                httpClient = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(
                            kotlinx.serialization.json.Json  {
                                ignoreUnknownKeys = true // if the server sends extra fields, ignore them
                            }
                        )
                    }
                }
            )
        }
    }
}