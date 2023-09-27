package com.ncc.domain.usecase.handover

import com.ncc.domain.model.DomainHandover
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class SetHandoverUsecase @Inject constructor(
    private val mainRepository: MainRepository
){
    fun execute(data: DomainHandover,team:String) = mainRepository.setHandover(data,team)
}