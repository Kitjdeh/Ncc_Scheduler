package com.ncc.domain.usecase

import com.ncc.domain.repository.SplashRepository
import javax.inject.Inject

class GetIsLogin @Inject constructor(
    private val splashRepository: SplashRepository
){
    suspend fun execute() = splashRepository.getIsLogin()
}