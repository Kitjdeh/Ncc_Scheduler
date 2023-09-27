package com.ncc.data.remote.model


data class DataRoutine(
    val name: String,
    val id: Int,
    val title: String,
    val team: String,
    val date: String,
) {
    constructor() : this("오류", 0,"오류", "0", "오류")
}


//data class Routine(
//    val name: String,
//    val id: Int,
//    val title: String,
//    val team: String,
//    val date: String,
//)