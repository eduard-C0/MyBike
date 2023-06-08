package com.example.mybike.di

import android.content.Context
import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideMyBikeDataBase(@ApplicationContext context: Context): MyBikeDataBase = MyBikeDataBase.getInstance(context)

    @Provides
    fun provideDefaultIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideDefaultScope(coroutineDispatcher: CoroutineDispatcher): CoroutineScope = CoroutineScope(coroutineDispatcher)
}