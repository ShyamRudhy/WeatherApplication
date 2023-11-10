package com.shyam.weatherapp.di

import android.content.Context
import com.shyam.weatherapp.base.PrivateSharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): PrivateSharedPrefManager {
        return PrivateSharedPrefManager(context)
    }


}
