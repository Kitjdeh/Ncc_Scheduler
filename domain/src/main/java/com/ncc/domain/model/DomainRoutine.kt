package com.ncc.domain.model

import java.time.DayOfWeek
import java.time.Month

data class DomainRoutine(
    val name: String,
    val id: Int,
    val title: String,
    val team: String,
    val date: String,
//    val month: Month,
//    val week: DayOfWeek,
//    val dayOfWeek: DayOfWeek
) {
    fun filtering (){

        //여기서 필터링 작업
    }
    constructor() : this("오류", 0, "오류", "0", "오류",)
}

