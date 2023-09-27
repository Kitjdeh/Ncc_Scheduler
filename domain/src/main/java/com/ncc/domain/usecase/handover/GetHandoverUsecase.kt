package com.ncc.domain.usecase.handover

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.toObject
import com.ncc.domain.model.DomainHandover
import com.ncc.domain.repository.MainRepository
import javax.inject.Inject

class GetHandoverUsecase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(date: String, team: String) = mainRepository.getHandover(date, team)
}