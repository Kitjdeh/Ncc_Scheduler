package com.ncc.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.ncc.domain.model.DomainComment
import com.ncc.domain.model.DomainHandover
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.model.DomainUser

interface MainRepository {

    //해당 날짜의 routine FireStore에서 호출
    suspend fun getRoutine(): List<DomainRoutine>
    suspend fun deleteRoutine(uid: String): Boolean

    suspend fun resetRoutine(): Boolean

    //    fun getRoutine(date: String): Task<QuerySnapshot>
    //해당 날짜 routine 등록
    fun setRoutine(data: DomainRoutine): Task<Void>

    //해당 날짜 인수인계 가져오기
    fun getHandover(date: String, team: String): Task<QuerySnapshot>

    //해당 날짜 인수인계 등록
    fun setHandover(data: DomainHandover, team: String): Task<Void>

    //인수인계에 댓글 달기
    fun setComments(comment: DomainComment, team: String): Task<Void>

    //유저 정보 가져오기
    fun getUserInfo(uid: String): Task<DocumentSnapshot>

    fun updateUserInfo(data: DomainUser): Task<Void>

}