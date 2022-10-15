package com.iliadmastery.country_datasource_test.cache

import com.iliadmastery.country_domain.Country

class CountryDatabaseFake {
    /**
     * Simulate database storage during testing
     */
    val countries: MutableList<Country> = mutableListOf()
}