package com.iliadmastery.country_interactors

import com.iliadmastery.country_datasource.cache.CountryCache
import com.iliadmastery.country_datasource.network.CountryService
import com.squareup.sqldelight.db.SqlDriver

/**
 * Class Interactor that contains all use-case possible
 *
 * @property getCountries : [GetCountries]
 * @property filterCountries : [FilterCountries]
 * @property getCountryFromCache : [GetCountryFromCache]
 * @property getLanguageFilterable: [GetLanguageFilterable]
 */
data class CountryInteractors(
    val getCountries: GetCountries,
    val filterCountries: FilterCountries,
    val getCountryFromCache: GetCountryFromCache,
    val getLanguageFilterable: GetLanguageFilterable
) {
    companion object Factory {
        fun build(sqlDriver: SqlDriver): CountryInteractors{
            val service = CountryService.build()
            val countryCache = CountryCache.build(sqlDriver)
            return CountryInteractors(
                getCountries = GetCountries(
                    cache = countryCache,
                    service = service,
                ),
                filterCountries = FilterCountries(),
                getCountryFromCache = GetCountryFromCache(
                    cache = countryCache
                ),
                getLanguageFilterable = GetLanguageFilterable(
                    cache = countryCache
                )
            )
        }

        val schema: SqlDriver.Schema = CountryCache.schema

        const val dbName: String = CountryCache.dbName
    }
}









