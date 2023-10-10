package com.ncc.presentation.widget.utils

import java.time.LocalDate

class Organization {

    companion object {
        val team = listOf(
            "A", "B", "C", "D"
        )
        val position = listOf(
            "교대 대리", "Board A", "Board B", "Board C", "Field A", "Field B", "Field C"
        )
        val viewPosition = listOf(
            "교대 대리", "Board A", "Board B", "Board C", "Field A", "Field B", "Field C", "전체보기"
        )
        val startDate = LocalDate.of(2023, 9, 2)

        val dayOfWeekList = listOf("", "일", "월", "화", "수", "목", "금", "토")

        val work = listOf("Daily", "Weekly", "Monthly")

        val shift = listOf("EVE","NIG","MOR")
    }
}


