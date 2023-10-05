package com.ncc.data.mapper

import com.ncc.data.remote.model.DataComment
import com.ncc.data.remote.model.DataHandover
import com.ncc.data.remote.model.DataRoutine
import com.ncc.data.remote.model.DataUser
import com.ncc.domain.model.DomainComment
import com.ncc.domain.model.DomainHandover
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.model.DomainUser

object MainMapper {
    fun routineMapper(
        domainRoutine: DomainRoutine
    ): DataRoutine {
        return DataRoutine(
            name = domainRoutine.name,
            id = domainRoutine.id,
            title = domainRoutine.title,
            team = domainRoutine.team,
            date = domainRoutine.date
        )
    }

    fun dataRoutineToDomainRoutine(
        dataRoutine: DataRoutine
    ):DomainRoutine{
        return DomainRoutine(
            name = dataRoutine.name,
            id = dataRoutine.id,
            title = dataRoutine.title,
            team = dataRoutine.team,
            date = dataRoutine.date
        )
    }

    fun domainCommentToDataComment(domainComment: DomainComment): DataComment {
        return DataComment(
            name = domainComment.name,
            handoverid = domainComment.handoverid,
            id = domainComment.id,
            content = domainComment.content,
            ImageSrc = domainComment.ImageSrc,
            time = domainComment.time,
            date = domainComment.date
        )
    }

    fun handoverMapper(
        domainHandover: DomainHandover
    ): DataHandover {
        val dataComment =
            domainHandover.comment?.map { domainCommentToDataComment(it) }
        return DataHandover(
            name = domainHandover.name,
            id = domainHandover.id,
            routine = domainHandover.routine,
            title = domainHandover.title,
            time = domainHandover.time,
            date = domainHandover.date,
            content = domainHandover.content,
            ImageSrc = domainHandover.ImageSrc,
            comment = dataComment,
            position = domainHandover.position
        )
    }

    fun userMapper(
        domainUser: DomainUser
    ): DataUser {
        return DataUser(
            id = domainUser.id,
            name = domainUser.name,
            team = domainUser.team,
            position = domainUser.position,
            uid = domainUser.uid

        )
    }
}
//val id: Int,
//val name: String,
//val team: String,
//val position: String,
//val uid: String,
//