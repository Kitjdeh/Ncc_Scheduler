package com.ncc.domain.model

import android.media.Image

data class DomainComment(
    val name: String,
    val handoverid: String,
    val id: String,
    val content: String,
    var ImageSrc: List<String>?,
    val date: String,
    val time: String,
) {
    constructor() : this("오류", "오류", "오류", "오류", emptyList(), "오류", "오류")
}
