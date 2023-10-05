package com.ncc.domain.usecase.routine

import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class GetRoutineUsecase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    //전체 업무 데이터 호출
    suspend fun execute(date: String) =
        mainRepository.getRoutine(date)
}
