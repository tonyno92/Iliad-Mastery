package com.iliadmastery.country_datasource.cache

import com.iliadmastery.country_datasource.cache.model.toCountry
import com.iliadmastery.country_domain.Country
import com.iliadmastery.countrydatasource.cache.CountryDbQueries
import com.iliadmastery.iliad_demo_datasource.cache.IliadDemoDatabase


class CountryCacheImpl(
    private val countryDatabase: IliadDemoDatabase,
) : CountryCache {

    private var queries: CountryDbQueries = countryDatabase.countryDbQueries

    override suspend fun getCountry(id: Long): Country {
        return queries.getCountry(id).executeAsOne().toCountry()
    }

    override suspend fun removeCountry(id: Long) {
        queries.removeCountry(id)
    }

    override suspend fun selectAll(): List<Country> {
        return queries.selectAll().executeAsList().map { it.toCountry() }
    }

    override suspend fun insert(country: Country) {
        return country.run {
            queries.insertCountry(
                id = id,
                name = name,
                /**
                 * Here it would have been possible to manage with an adapter as for [CountryAssignedStatusEnum]
                 * being a very simple management, we avoid loading a new adapter in the DB
                 */
                continents = continents.joinToString(";"),
                languages = languages.joinToString(";"),
                flag = flag,
                coatOfArms = coatOfArms,
                area = area,
                population = population,
                unmember = unMember,
                status = status
            )
        }
    }

    override suspend fun insert(countries: List<Country>) {
        for (country in countries) {
            try {
                insert(country)
            } catch (e: Exception) {
                e.printStackTrace()
                // on error continue with others
            }
        }
    }

    override suspend fun searchByName(name: String): List<Country> {
        return queries.searchCountryByName(name).executeAsList().map { it.toCountry() }
    }

    override suspend fun searchByContinent(continent: String): List<Country> {
        return queries.searchCountryByContinent(continent).executeAsList().map { it.toCountry() }
    }

    override suspend fun searchByLanguage(language: String): List<Country> {
        return queries.searchCountryByLanguage(language).executeAsList().map { it.toCountry() }
    }

}














