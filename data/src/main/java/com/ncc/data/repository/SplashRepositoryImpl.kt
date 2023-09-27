package com.ncc.data.repository


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import com.ncc.data.repository.local.datasource.SplashLocalDataSource
import com.ncc.data.repository.remote.datasource.SplashDataSource
import com.ncc.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val splashDatatSource: SplashDataSource,
    private val splashLocalDataSource: SplashLocalDataSource,

    ) : SplashRepository {


    override fun checkAppVersion(): Task<DataSnapshot> {
        return splashDatatSource.checkAppVersion()
    }

    override fun login(id: String, pwd: String): Task<AuthResult> {
        return splashDatatSource.login(id, pwd)
    }

    override suspend fun saveUser(id: String, pwd: String, uid: String) {
        return splashLocalDataSource.saveUser(id, pwd, uid)
    }

    override suspend fun getUser(): Flow<List<String>> {
        return splashLocalDataSource.getUser()
    }

    override suspend fun getIsLogin(): Flow<Boolean> {
        return splashLocalDataSource.getIsLogin()
    }

    override suspend fun logout(): Flow<Boolean> {
        splashDatatSource.logout()

        return splashLocalDataSource.logout()
    }


}