package com.ncc.data.repository.local.datasource

import kotlinx.coroutines.flow.Flow

interface SplashLocalDataSource {

    suspend fun saveUser(id: String, pwd: String, uid: String)

    suspend fun getUser(): Flow<List<String>>

    suspend fun getIsLogin(): Flow<Boolean>

    suspend fun logout(): Flow<Boolean>
}