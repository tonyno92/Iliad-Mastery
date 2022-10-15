package com.iliadmastery.country_datasource_test.network

import com.iliadmastery.country_datasource.network.CountryService
import com.iliadmastery.country_datasource.network.CountryServiceImpl
import com.iliadmastery.country_datasource_test.network.data.CountryDataEmpty
import com.iliadmastery.country_datasource_test.network.data.CountryDataMalformed
import com.iliadmastery.country_datasource_test.network.data.CountryDataValid
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

class CountryServiceFake {

    companion object Factory {

        private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
        private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

        fun build(
            type: CountryServiceResponseType
        ): CountryService {
            val client = HttpClient(MockEngine) {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(
                        kotlinx.serialization.json.Json {
                            // if the server sends extra fields, ignore them
                            ignoreUnknownKeys = true
                        }
                    )
                }
                engine {
                    addHandler { request ->
                        when (request.url.fullUrl) {
                            "https://restcountries.com/v3.1/all" -> {
                                val responseHeaders = headersOf(
                                    "Content-Type" to listOf("application/json", "charset=utf-8")
                                )
                                when (type) {
                                    is CountryServiceResponseType.EmptyList -> {
                                        respond(
                                            CountryDataEmpty.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is CountryServiceResponseType.ErrorOnJsonData -> {
                                        respond(
                                            CountryDataMalformed.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is CountryServiceResponseType.CorrectData -> {
                                        respond(
                                            CountryDataValid.data,
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is CountryServiceResponseType.Http404 -> {
                                        respond(
                                            CountryDataEmpty.data,
                                            status = HttpStatusCode.NotFound,
                                            headers = responseHeaders
                                        )
                                    }
                                }
                            }
                            else -> error("Unhandled ${request.url.fullUrl}")
                        }
                    }
                }
            }
            return CountryServiceImpl(client)
        }
    }
}