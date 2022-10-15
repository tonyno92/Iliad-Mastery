package com.iliadmastery.ui_countrydetail.di

import com.iliadmastery.country_interactors.CountryInteractors
import com.iliadmastery.country_interactors.GetCountryFromCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CountryDetailModule {

    /**
     * @param interactors is provided in app module.
     */
    @Provides
    @Singleton
    fun provideGetCountryFromCache(
        interactors: CountryInteractors
    ): GetCountryFromCache {
        return interactors.getCountryFromCache
    }
}