package com.iliadmastery.demo.di

import android.app.Application
import com.iliadmastery.country_interactors.CountryInteractors
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module that provide app interactos
 */
@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {

    @Provides
    @Singleton
    @Named("countryAndroidSqlDriver") // in case you had another SQL Delight db
    fun provideAndroidDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = CountryInteractors.schema,
            context = app,
            name = CountryInteractors.dbName
        )
    }

    @Provides
    @Singleton
    fun provideCountryInteractors(
        @Named("countryAndroidSqlDriver") sqlDriver: SqlDriver,
    ): CountryInteractors {
        return CountryInteractors.build(sqlDriver)
    }
}


