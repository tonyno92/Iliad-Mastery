package com.iliadmastery.country_datasource.network.model

import com.iliadmastery.country_domain.Country
import com.iliadmastery.country_domain.CountryAssignedStatusEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Country Data transfer object
 *
 * @property name [NameDto]
 * @property continents
 * @property languages
 * @property flags
 * @property coatOfArms
 * @property area
 * @property population
 * @property unMember
 * @property status
 */
@Serializable
data class CountryDto(

    @SerialName("name")
    val name: NameDto,

    @SerialName("continents")
    val continents: List<String>,

    @SerialName("languages")
    val languages: Map<String, String> = emptyMap(),

    @SerialName("flags")
    val flags: FlagDto,

    @SerialName("coatOfArms")
    val coatOfArms: CoatOfArmsDto,

    @SerialName("area")
    val area: Float,

    @SerialName("population")
    val population: Int,

    @SerialName("unMember")
    val unMember: Boolean,

    @SerialName("status")
    val status: String,
)

@Serializable
data class NameDto(
    @SerialName("common")
    val common: String,
    @SerialName("official")
    val official: String,
)

@Serializable
data class FlagDto(
    @SerialName("png")
    val png: String,
)

@Serializable
data class CoatOfArmsDto(
    @SerialName("png")
    val png: String? = "",
)

/**
 * Mapping function from data layer object to business model [Country]
 * This is a bridge from data layer to domain layer.
 * Usually as best practice we use a repository to abstract the logic, but being a single model
 * for the whole app, for simplicity we use a mapping method.
 * @return [Country]
 */
fun CountryDto.toCountry(): Country {
    return Country(
        id = name.official.hashCode().toLong(),
        name = name.official,
        continents = continents,
        flag = flags.png,
        coatOfArms = coatOfArms.png,
        languages = languages.values.toList(),
        area = area,
        population = population,
        unMember = unMember,
        status = CountryAssignedStatusEnum.from(status),
    )
}























