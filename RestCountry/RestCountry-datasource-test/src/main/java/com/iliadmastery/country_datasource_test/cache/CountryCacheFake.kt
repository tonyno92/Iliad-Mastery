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
    override suspend fun getCountry(id: Long) =
        db.countries.find { it.id == id }

    /**
     * @see [CountryCache]
     */
    override suspend fun removeCountry(id: Long) {
        db.countries.remove(db.countries.find { it.id == id })
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
        if (db.countries.isNotEmpty()) {
            var didInsert = false
            for (cnt in db.countries) {
                if (cnt.id == country.id) {
                    db.countries.remove(cnt)
                    db.countries.add(country)
                    didInsert = true
                    break
                }
            }
            if (!didInsert) {
                db.countries.add(country)
            }
        } else {
            db.countries.add(country)
        }
    }

    /**
     * @see [CountryCache]
     */
    override suspend fun insert(countries: List<Country>) {
        if (db.countries.isNotEmpty()) {
            for (country in countries) {
                if (db.countries.contains(country)) {
                    db.countries.remove(country)
                    db.countries.add(country)
                }
            }
        } else {
            db.countries.addAll(countries)
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













