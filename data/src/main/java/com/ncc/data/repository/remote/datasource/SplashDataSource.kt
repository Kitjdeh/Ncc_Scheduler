package com.ncc.data.repository.remote.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface SplashDataSource {
    fun checkAppVersion(): Task<DataSnapshot>

    //로그인
    fun login(id: String, pwd: String): Task<AuthResult>

    // 로그아웃
    suspend fun logout(): Flow<Boolean>
}