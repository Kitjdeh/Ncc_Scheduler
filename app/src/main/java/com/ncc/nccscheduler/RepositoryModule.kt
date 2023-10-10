package com.ncc.nccscheduler

import com.ncc.data.repository.MainRepositoryImpl
import com.ncc.data.repository.SplashRepositoryImpl
import com.ncc.data.repository.local.datasource.SplashLocalDataSource
import com.ncc.data.repository.remote.datasource.MainDataSource
import com.ncc.data.repository.remote.datasource.SplashDataSource
import com.ncc.domain.repository.MainRepository
import com.ncc.domain.repository.SplashRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMainRepository(
        mainDataSource: MainDataSource
    ): MainRepository {
        return MainRepositoryImpl(
            mainDataSource
        )
    }

    @Provides
    @Singleton
    fun provideSplashRepository(
        splashDatatSource: SplashDataSource,
        splashLocalDataSource: SplashLocalDataSource,
    ): SplashRepository {
        return SplashRepositoryImpl(
            splashDatatSource,
            splashLocalDataSource
        )
    }



}