package com.iliadmastery.core.domain

/**
 * Rappresents Asc or Desc ordering
 */
sealed class FilterOrder {

    object Ascending: FilterOrder()

    object Descending: FilterOrder()
}
