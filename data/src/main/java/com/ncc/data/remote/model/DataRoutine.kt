package com.ncc.data.remote.model

import java.time.DayOfWeek


data class DataRoutine(
    val name: String,
    var id: String,
    val title: String,
    val position: List<String>,
    val team: String,
    val content: String,
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
        "오류", emptyList(),
        "0",
        "오류",
        "오류",
        "오류",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )
}


//data class Routine(
//    val name: String,
//    val id: Int,
//    val title: String,
//    val team: String,
//    val date: String,
//)