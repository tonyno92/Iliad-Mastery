package com.iliadmastery.country_domain

/**
 * Business\Domain model of a Country
 *
 * @property id
 * @property name
 * @property continents
 * @property flag
 * @property coatOfArms
 * @property languages
 * @property area
 * @property population
 * @property unMember
 * @property status
 */
data class Country(
    val id: Long,
    val name: String,
    val continents: List<String>,
    val flag: String,
    val coatOfArms: String?,
    val languages: List<String>,
    val area: Float,
    val population: Int,
    val unMember: Boolean,
    val status: CountryAssignedStatusEnum,
)

