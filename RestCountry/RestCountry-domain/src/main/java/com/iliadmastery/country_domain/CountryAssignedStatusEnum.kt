package com.iliadmastery.country_domain

/**
 * Enum rappresenting assigned status
 *
 * @property value [String]
 */
enum class CountryAssignedStatusEnum(private val value: String) {
    OFFICIALLY("officially-assigned"),
    USER("user-assigned"),
    UNKNOWN("unknown");

    companion object {
        fun from(value: String) = values().find { it.value == value } ?: UNKNOWN
    }
}