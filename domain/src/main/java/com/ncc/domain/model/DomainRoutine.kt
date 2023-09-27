package com.ncc.domain.model

data class DomainRoutine(
    val name: String,
    val id: Int,
    val title: String,
    val team: String,
    val date: String,
) {
    constructor() : this("오류", 0, "오류", "0", "오류")
}

