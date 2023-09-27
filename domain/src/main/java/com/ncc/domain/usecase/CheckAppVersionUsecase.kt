package com.ncc.domain.usecase

import com.ncc.domain.repository.SplashRepository
import javax.inject.Inject

class CheckAppVersionUseCase @Inject constructor(
    private val splashRepository: SplashRepository
){
    fun execute() = splashRepository.checkAppVersion()
}