package com.ncc.nccscheduler

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ncc.data.repository.local.datasource.SplashLocalDataSource
import com.ncc.data.repository.local.datasourceImpl.SplashLocalDataSourceImpl
import com.ncc.data.repository.remote.datasource.MainDataSource
import com.ncc.data.repository.remote.datasource.SplashDataSource
import com.ncc.data.repository.remote.datasourceImpl.MainDataSourceImpl
import com.ncc.data.repository.remote.datasourceImpl.SplashDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun provideMainDataSource(
        firestore: FirebaseFirestore,
        stroage: FirebaseStorage,
        auth: FirebaseAuth
    ): MainDataSource {
        return MainDataSourceImpl(
            firestore, stroage, auth
        )
    }

    @Provides
    @Singleton
    fun provideSpalshDataSource(
        firebaseRtdb: FirebaseDatabase,
        fireStore: FirebaseFirestore,
        auth: FirebaseAuth
    ): SplashDataSource {
        return SplashDataSourceImpl(
            firebaseRtdb, fireStore, auth
        )
    }

    @Provides
    @Singleton
    fun provideSpalshLocalDataSource(
        firebaseRtdb: FirebaseDatabase,
        fireStore: FirebaseFirestore,
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): SplashLocalDataSource {
        return SplashLocalDataSourceImpl(
            firebaseRtdb, fireStore, auth, context
        )
    }
}

