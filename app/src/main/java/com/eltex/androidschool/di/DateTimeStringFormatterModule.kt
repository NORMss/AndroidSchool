package com.eltex.androidschool.di

import com.eltex.androidschool.view.util.datetime.DateTimeStringFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DateTimeStringFormatterModule {
    @Provides
    fun provideDateTimeStringFormatter(): DateTimeStringFormatter {
        return DateTimeStringFormatter.default()
    }
}