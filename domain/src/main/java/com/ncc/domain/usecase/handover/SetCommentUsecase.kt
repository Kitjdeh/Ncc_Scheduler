package com.ncc.domain.usecase.handover

import com.ncc.domain.model.DomainComment
import com.ncc.domain.model.DomainHandover
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class SetCommentUsecase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(data: DomainComment, team: String) = mainRepository.setComments(data, team)
}