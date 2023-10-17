package com.ncc.domain.usecase.routine

import android.util.Log
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.repository.MainRepository
import kotlinx.coroutines.coroutineScope
import java.time.DayOfWeek
import javax.inject.Inject

class GetRoutineUsecase @Inject constructor(
    private val mainRepository: MainRepository,
) {
    private lateinit var totalRoutine: List<DomainRoutine>


    //Firebase에서 전체 데이터 호출
    suspend fun getRoutine(): List<DomainRoutine> {
        totalRoutine = mainRepository.getRoutine()
        return totalRoutine
    }

    suspend fun deleteRoutine(uid: String): Boolean = mainRepository.deleteRoutine(uid)

    suspend fun resetRoutine(): Boolean = mainRepository.resetRoutine()

    //team, date 에 해당하는 업무 filtering 작업
    suspend fun execute(
        position: String,
        team: String,
        date: String,
        dayOfWeek: String,
        week: String
    ): List<DomainRoutine> {
        return coroutineScope {
            var isMatched = false
            val routineList = totalRoutine.filter {
                if (it.team == team && it.position.contains(position)) {
                    when (it.type) {
                        "Monthly" -> {
                            // 요일 단위 체크 일경우
                            isMatched = if (it.dayOfMonth.isNullOrEmpty()) {
                                it.dayOfWeek!!.contains(dayOfWeek)
                            } else {
                                val day = date.takeLast(2)
                                it.dayOfMonth!!.contains(day)
                            }
                            isMatched
                        }
                        //weekly 필터 작업
                        "Weekly" -> {
                            isMatched =
                                it.dayOfWeek!!.contains(dayOfWeek) && it.week!!.contains(week)
                            isMatched
                        }
                        //daily 필터 작업
                        "Daily" -> {
                            true
                        }

                        else -> {
                            false
                        }
                    }
                } else {
                    false
                }
            }
            routineList
        }
    }
}
