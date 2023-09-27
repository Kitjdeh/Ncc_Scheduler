package com.ncc.domain.usecase.routine

import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class GetRoutineUsecase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(date: String) =

        mainRepository.getRoutine(date)
}
