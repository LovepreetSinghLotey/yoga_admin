package com.example.universal_yoga_admin.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceDI {

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            "com.example.universal_yoga_admin",
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun providePreferenceUtil(prefs: SharedPreferences): PreferenceUtil =
        PreferenceUtil(prefs)
}