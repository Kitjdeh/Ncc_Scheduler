package com.ncc.domain.usecase

import com.ncc.domain.repository.SplashRepository
import javax.inject.Inject

class SaveIdUsecase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    suspend fun execute(id: String, pwd: String, uid: String) =
        splashRepository.saveUser(id, pwd, uid)
}