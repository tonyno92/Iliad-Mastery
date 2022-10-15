package com.iliadmastery.demo.di

import com.iliadmastery.core.util.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that provide horizontally components such as Logger, Analitycs and so on
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger{
        return Logger(
            tag = "AppDebug",
            isDebug = true
        )
    }
}