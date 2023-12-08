package com.example.universal_yoga_admin.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbHelperDI {

    @Singleton
    @Provides
    fun provideCourseDbHelper(@ApplicationContext context: Context): CourseDbHelper =
        CourseDbHelper(context, "uniYogaDb", 3)

    @Singleton
    @Provides
    fun provideClassDbHelper(@ApplicationContext context: Context): ClassDbHelper =
        ClassDbHelper(context, "uniYogaClassDb", 3)
}