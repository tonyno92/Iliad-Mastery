package com.iliadmastery.country_datasource_test.cache

import com.iliadmastery.country_datasource.cache.CountryCache
import com.iliadmastery.country_domain.Country

/**
 * Fake Cache that implements [CountryCache] for test
 *
 * @property db
 */
class CountryCacheFake(
    private val db: CountryDatabaseFake
) : CountryCache {

    /**
     * @see [CountryCache]
     */
    override suspend fun getCountry(id: Int) =
        db.countries.find { it.id == id }

    /**
     * @see [CountryCache]
     */
    override suspend fun removeCountry(id: Int) {
        db.countries.apply { remove(find { it.id == id }) }
    }

    /**
     * @see [CountryCache]
     */
    override suspend fun selectAll() =
        db.countries

    /**
     * @see [CountryCache]
     */
    override suspend fun insert(country: Country) {
        db.countries.apply {
            if (isNotEmpty()) {
                var didInsert = false
                for (cnt in this) {
                    if (cnt.id == country.id) {
                        remove(cnt)
                        add(country)
                        didInsert = true
                        break
                    }
                }
                if (!didInsert) {
                    add(country)
                }
            } else {
                add(country)
            }
        }
    }

    /**
     * @see [CountryCache]
     */
    override suspend fun insert(countries: List<Country>) {
        db.countries.apply {
            if (isNotEmpty()) {
                for (country in countries) {
                    if (contains(country)) {
                        remove(country)
                        add(country)
                    }
                }
            } else {
                addAll(countries)
            }
        }
    }

    /**
     * @see [CountryCache]
     */
    override suspend fun searchByName(name: String) = db.countries.filter { it.name.contains(name) }

    /**
     * @see [CountryCache]
     */
    override suspend fun searchByContinent(continent: String) =
        db.countries.filter { it.continents.contains(continent) }

    /**
     * @see [CountryCache]
     */
    override suspend fun searchByLanguage(language: String) =
        db.countries.filter { it.languages.contains(language) }

}













