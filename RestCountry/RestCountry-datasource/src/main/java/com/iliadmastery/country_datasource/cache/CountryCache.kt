package com.iliadmastery.country_datasource.cache

import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_domain.CountryAssignedStatusEnum
import com.iliadmastery.countrydatasource.cache.Country_Entity
import com.iliadmastery.iliad_demo_datasource.cache.IliadDemoDatabase
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver

/**
 * Cache Country Interface
 */
interface CountryCache {

    /**
     * Retrive country from cache by primary key
     *
     * @param id
     * @return [Country]?
     */
    suspend fun getCountry(id: Long): Country?

    /**
     * remove country from cache by primary key
     *
     * @param id
     */
    suspend fun removeCountry(id: Long)

    /**
     * select all countries from cache
     *
     * @return List<Country>
     */
    suspend fun selectAll(): List<Country>

    /**
     * insert new country into cache
     *
     * @param country
     */
    suspend fun insert(country: Country)

    /**
     * insert a list of country into cache
     *
     * @param countries
     */
    suspend fun insert(countries: List<Country>)

    /**
     * search country by name
     *
     * @param name
     * @return List<[Country]>
     */
    suspend fun searchByName(name: String): List<Country>

    /**
     * search country by continent
     *
     * @param continent
     * @return List<[Country]>
     */
    suspend fun searchByContinent(continent: String): List<Country>

    /**
     * search country by language
     *
     * @param language
     * @return List<[Country]>
     */
    suspend fun searchByLanguage(language: String): List<Country>

    /**
     * Factory cache if type DbCache
     */
    companion object Factory {

        /**
         * Adapter that convert string value to CountryAssignedStatusEnum and viceversa into DB
         */
        private val countryAssignedStatusAdapter =
            Country_Entity.Adapter(
                object : ColumnAdapter<CountryAssignedStatusEnum, String> {
                    override fun decode(databaseValue: String) =
                        CountryAssignedStatusEnum.valueOf(databaseValue)

                    override fun encode(value: CountryAssignedStatusEnum) = value.name
                }
            )

        /**
         * Build cache with specific driver
         *
         * @param sqlDriver
         * @return [CountryCache]
         */
        fun build(sqlDriver: SqlDriver): CountryCache {
            return CountryCacheImpl(IliadDemoDatabase(sqlDriver, countryAssignedStatusAdapter))
        }

        val schema: SqlDriver.Schema = IliadDemoDatabase.Schema

        const val dbName: String = "countries.db"
    }

}



















