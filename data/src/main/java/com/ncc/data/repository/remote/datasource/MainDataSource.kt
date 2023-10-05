package com.ncc.data.repository.remote.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.ncc.data.remote.model.DataComment
import com.ncc.data.remote.model.DataHandover
import com.ncc.data.remote.model.DataRoutine
import com.ncc.data.remote.model.DataUser

interface MainDataSource {

    //해당 날짜의 routine 가져오기
    suspend fun getRoutine(date: String): List<DataRoutine>

//    이전꺼
//    fun getRoutine(date: String): Task<QuerySnapshot>

    //해당 날짜 routine 등록
    fun setRoutine(data: DataRoutine): Task<Void>

    //해당 날짜 인수인계 가져오기
    fun getHandover(date: String, team: String): Task<QuerySnapshot>

    //해당 날짜 인수인계 등록
    fun setHandover(data: DataHandover, team: String): Task<Void>

    //인수인계에 댓글 달기
    fun setComments(comment: DataComment, team: String): Task<Void>

    //유저 정보 호출
    fun getUserInfo(uid: String) : Task<DocumentSnapshot>

    //유저 정보 수정
    fun updateUserInfo(data: DataUser): Task<Void>

}