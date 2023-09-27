package com.ncc.data.remote.model

import android.media.Image
import android.net.Uri

data class DataHandover(
    var name: String,
    var id: String,
    var routine: String,
    var title: String,
    var date : String,
    var time : String,
    var content: String,
    var ImageSrc : List<String>?,
    var comment: List<DataComment>?
    ,
    var position: String
)