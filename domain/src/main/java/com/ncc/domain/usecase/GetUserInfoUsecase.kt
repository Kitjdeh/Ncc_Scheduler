package com.ncc.domain.usecase

import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class GetUserInfoUsecase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(uid: String) = mainRepository.getUserInfo(uid)

}
