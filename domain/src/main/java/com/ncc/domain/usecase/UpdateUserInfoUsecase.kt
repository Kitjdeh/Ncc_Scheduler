package com.ncc.domain.usecase

import com.ncc.domain.model.DomainUser
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class UpdateUserInfoUsecase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(data: DomainUser) = mainRepository.updateUserInfo(data)
}