package com.ncc.data.repository.local.datasourceImpl

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.ncc.data.repository.SplashRepositoryImpl
import com.ncc.data.repository.local.datasource.SplashLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class SplashLocalDataSourceImpl @Inject constructor(
    private val firebaseRtdb: FirebaseDatabase,
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
//    @ApplicationContext private val context: Context
    private val context: Context
) : SplashLocalDataSource {

    object PreferenceKeys {
        val LOGIN_ID = stringPreferencesKey("id")
        val LOGIN_PWD = stringPreferencesKey("pwd")
        val LOGIN_UID = stringPreferencesKey("uid")
        val LOGIN_CHECK = booleanPreferencesKey("login_check")
    }

    override suspend fun logout(): Flow<Boolean> {
        return flow {
            try {
                context.loginCheckDataStore.edit { prefs ->
                    prefs[SplashLocalDataSourceImpl.PreferenceKeys.LOGIN_CHECK] = false
                }
                emit(true) // 변경 성공
            } catch (e: Exception) {
                emit(false) // 변경 실패
            }
        }
    }

    private val Context.saveUser by preferencesDataStore(name = "USER_DATASTORE")
    private val Context.loginCheckDataStore by preferencesDataStore(name = "LOGIN_CHECK_DATASTORE")


    override suspend fun saveUser(id: String, pwd: String, uid: String) {
        if (id != null && pwd != null) {
            context.saveUser.edit { prefs ->
                prefs[SplashLocalDataSourceImpl.PreferenceKeys.LOGIN_ID] = id
                prefs[SplashLocalDataSourceImpl.PreferenceKeys.LOGIN_PWD] = pwd
                prefs[SplashLocalDataSourceImpl.PreferenceKeys.LOGIN_UID] = uid
            }

            context.loginCheckDataStore.edit { prefs ->
                prefs[SplashLocalDataSourceImpl.PreferenceKeys.LOGIN_CHECK] = true
            }
        }
    }

    override suspend fun getUser(): Flow<List<String>> {
        return context.saveUser.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs.asMap().values.toList().map {
                    it.toString()
                }
            }
    }

    override suspend fun getIsLogin(): Flow<Boolean> {
        return context.loginCheckDataStore.data
            .map { prefs ->
                prefs[SplashLocalDataSourceImpl.PreferenceKeys.LOGIN_CHECK] ?: false
            }
    }


}