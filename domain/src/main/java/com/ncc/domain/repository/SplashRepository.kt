package com.ncc.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface SplashRepository {
    fun checkAppVersion(): Task<DataSnapshot>


    //로그인
    fun login(id: String, pwd: String): Task<AuthResult>

    suspend fun saveUser(id: String, pwd: String, uid: String)

    suspend fun getUser(): Flow<List<String>>

    suspend fun getIsLogin(): Flow<Boolean>

    suspend fun logout(): Flow<Boolean>
}