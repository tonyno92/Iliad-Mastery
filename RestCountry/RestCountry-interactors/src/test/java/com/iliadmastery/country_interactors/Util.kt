package com.iliadmastery.country_interactors

import com.iliadmastery.country_domain.Country

/**
 * Simulate id auto increment (start at zero)
 */
fun List<Country>.simulateIdAutoIncrement() =
    this.mapIndexed { index, country -> country.copy(id = index.toLong()) }
