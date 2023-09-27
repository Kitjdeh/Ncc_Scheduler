package com.ncc.data.repository.remote.datasourceImpl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.ncc.data.repository.remote.datasource.SplashDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashDataSourceImpl @Inject constructor(
    private val firebaseRtdb: FirebaseDatabase,
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SplashDataSource {
    override fun checkAppVersion(): Task<DataSnapshot> {
        return firebaseRtdb.reference.child("version").get()
    }

    override fun login(id: String, pwd: String): Task<AuthResult> {

        return auth.signInWithEmailAndPassword(id, pwd)
    }

    override suspend fun logout(): Flow<Boolean> {
        return flow {
            try {
                auth.signOut()
                emit(true)
            } catch (e: Exception) {
                emit(false)
            }
        }
    }
}
