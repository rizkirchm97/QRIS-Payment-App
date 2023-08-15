package com.rizki.qrispayment.di

import com.rizki.qrispayment.data.datasource.cache.DataStoreCached
import com.rizki.qrispayment.data.datasource.cache.DataStoreCachedImpl
import com.rizki.qrispayment.data.datasource.local.LocalDataSource
import com.rizki.qrispayment.data.datasource.local.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * created by RIZKI RACHMANUDIN on 15/08/2023
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    @Singleton
    fun bindDataStoreCached(dataStoreCachedImpl: DataStoreCachedImpl): DataStoreCached
}