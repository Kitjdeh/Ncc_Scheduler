package com.ncc.domain.usecase

import com.ncc.domain.repository.MainRepository
import com.ncc.domain.repository.SplashRepository
import javax.inject.Inject

class LoginUsecase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    fun execute(id: String, pwd: String) = splashRepository.login(id, pwd)
}


