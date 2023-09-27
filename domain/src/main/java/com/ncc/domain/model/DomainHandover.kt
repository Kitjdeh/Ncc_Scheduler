package com.ncc.domain.model

import android.media.Image
import android.net.Uri

data class DomainHandover(
    var name: String,
    var id: String,
    var routine: String,
    var title: String,
    var date: String,
    var time: String,
    var content: String,
    var ImageSrc: List<String>?,
    var comment: List<DomainComment>?,
    var position: String
) {
    constructor() : this("오류", "오류", "오류", "0", "오류", "오류", "오류", emptyList(), emptyList(),"")
}