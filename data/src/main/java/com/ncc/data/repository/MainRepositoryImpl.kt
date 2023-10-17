package com.ncc.data.repository


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.ncc.data.mapper.MainMapper
import com.ncc.data.repository.remote.datasource.MainDataSource
import com.ncc.domain.model.DomainComment
import com.ncc.domain.model.DomainHandover
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.model.DomainUser
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainDataSource: MainDataSource

) : MainRepository {

    //해당 날짜의 routine 가져오기
    override suspend fun getRoutine(): List<DomainRoutine> {
        val data = mainDataSource.getRoutine()
        val result = arrayListOf<DomainRoutine>()
        for (item in data) {
            result.add(MainMapper.dataRoutineToDomainRoutine(item))
        }
        return result
    }

    //    override fun getRoutine(date: String): Task<QuerySnapshot> {
//        val result = mainDataSource.getRoutine(date)
//
//        return result
//    }
    //해당 날짜 인수인계 가져오기
    override suspend fun deleteRoutine(uid: String): Boolean {
        return mainDataSource.deleteRoutine(uid)
    }

    override suspend fun resetRoutine(): Boolean {
        return mainDataSource.resetRoutine()
    }

    override fun getHandover(date: String, team: String): Task<QuerySnapshot> {
        return mainDataSource.getHandover(date, team)
    }

    //해당 날짜 routine 등록
    override fun setRoutine(data: DomainRoutine): Task<Void> {
        return mainDataSource.setRoutine(MainMapper.routineMapper(data))
    }

    //해당 날짜 인수인계 등록
    override fun setHandover(data: DomainHandover, team: String): Task<Void> {
        return mainDataSource.setHandover(MainMapper.handoverMapper(data), team)
    }

    //인수인계에 댓글 달기
    override fun setComments(comment: DomainComment, team: String): Task<Void> {
        return mainDataSource.setComments(MainMapper.domainCommentToDataComment(comment), team)
    }

    override fun getUserInfo(uid: String): Task<DocumentSnapshot> {
        return mainDataSource.getUserInfo(uid)
    }

    override fun updateUserInfo(data: DomainUser): Task<Void> {
        return mainDataSource.updateUserInfo(MainMapper.userMapper(data))
    }
}