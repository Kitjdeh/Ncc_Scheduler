package com.ncc.domain.usecase.routine

import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class SetRoutineUsecase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(data: DomainRoutine) = mainRepository.setRoutine(data)
}