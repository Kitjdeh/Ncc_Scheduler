package com.ncc.domain.model

import java.time.DayOfWeek
import java.time.Month

data class DomainRoutine(
    val name: String,
    val id: String,
    val position: List<String>,
    val title: String,
    val content: String,
    val team: String,
    val date: String,
    val type: String,
    val month: List<String>?,
    val week: List<String>?,
    val dayOfMonth: List<String>?,
    val dayOfWeek: List<String>?,
) {
    constructor() : this(
        "오류",
        "",
        emptyList(), "오류", "오류",
        "0",
        "오류",
        "오류",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )
}