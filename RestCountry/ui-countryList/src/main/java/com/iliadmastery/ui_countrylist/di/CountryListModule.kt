package com.iliadmastery.ui_countrylist.di

import com.iliadmastery.country_interactors.CountryInteractors
import com.iliadmastery.country_interactors.FilterCountries
import com.iliadmastery.country_interactors.GetCountries
import com.iliadmastery.country_interactors.GetLanguageFilterable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that provide all the use-case of an [CountryInteractors]
 */
@Module
@InstallIn(SingletonComponent::class)
object CountryListModule {

    /**
     * @param interactors is provided in app module.
     */
    @Provides
    @Singleton
    fun provideGetCountries(
        interactors: CountryInteractors
    ): GetCountries {
        return interactors.getCountries
    }

    @Provides
    @Singleton
    fun provideFilterCountries(
        interactors: CountryInteractors
    ): FilterCountries {
        return interactors.filterCountries
    }

    @Provides
    @Singleton
    fun provideFilterLanguage(
        interactors: CountryInteractors
    ): GetLanguageFilterable {
        return interactors.getLanguageFilterable
    }
}