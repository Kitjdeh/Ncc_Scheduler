package com.ncc.data.remote.model

import android.media.Image

data class DataComment(
    val name : String,
    val handoverid : String,
    val id : String,
    val content: String,
    var ImageSrc : List<String>?,
    val date : String,
    val time : String,
)
