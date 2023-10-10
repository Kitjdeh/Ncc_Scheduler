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
        Log.d("도메인 routineList요청", totalRoutine.toString())
        return totalRoutine
    }

    //team, date 에 해당하는 업무 filtering 작업
    suspend fun execute(team: String, date: String, dayOfWeek: String): List<DomainRoutine> {
        return coroutineScope {
            var isMatched = false
            Log.d("도메인 routineList", totalRoutine.toString())
            Log.d("조건${dayOfWeek} ", "${date.takeLast(2)} ${team}")
            val routineList = totalRoutine.filter {
                if (it.team == team) {
                    // monthly 필터 작업
                    if (it.type == "Monthly") {
                        // 요일 단위 체크 일경우
                        isMatched = if (it.dayOfMonth.isNullOrEmpty()) {
                            Log.d("dayOfWeek", it.dayOfWeek.toString())
                            it.dayOfWeek!!.contains(dayOfWeek)
                        } else {
                            val day = date.takeLast(2)
                            it.dayOfMonth!!.contains(day)
                        }
                        isMatched
                    }
                    //weekly 필터 작업
                    else if (it.type == "Weekly") {
                        isMatched =
                            it.dayOfWeek!!.contains(dayOfWeek)
                        isMatched
                    }
                    //daily 필터 작업
                    else if (it.type == "Daily") {
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
            routineList
        }
    }
}
