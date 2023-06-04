package com.example.mybike.di

import android.content.Context
import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideMyBikeDataBase(@ApplicationContext context: Context): MyBikeDataBase = MyBikeDataBase.getInstance(context)

}