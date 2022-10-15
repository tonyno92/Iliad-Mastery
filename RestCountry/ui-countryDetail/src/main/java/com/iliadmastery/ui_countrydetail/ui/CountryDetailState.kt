package com.iliadmastery.ui_countrydetail.ui

import com.iliadmastery.core.domain.ProgressBarState
import com.iliadmastery.core.domain.Queue
import com.iliadmastery.core.domain.UIComponent
import com.iliadmastery.country_domain.Country

/**
 * Possible states of Country Detail Screen
 *
 * @property progressBarState [ProgressBarState]
 * @property country [Country]
 * @property errorQueue Queue[UIComponent]
 */
data class CountryDetailState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val country: Country? = null,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
)
