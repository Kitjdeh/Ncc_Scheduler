package com.ncc.presentation.adapter.handover

import android.widget.TextView
import androidx.databinding.BindingAdapter

object HandoverBindingAdapter {

    @JvmStatic
    @BindingAdapter("set_time")
    fun settime(text: TextView, time: String) {
        val endIndex = time.length
        val startIndex = endIndex - 12
        val minute = time.substring(endIndex - 2, endIndex)
        val hour = time.substring(endIndex - 5, endIndex - 3)

        val date = time.substring(startIndex, startIndex + 5)
        val firstText = time.substring(startIndex, endIndex)
        text.text = "${date} ${hour}:${minute}"
//        val secondText  =
//        text.text = time.substring(startIndex, endIndex)
//        text.text = time
    }

    @JvmStatic
    @BindingAdapter("set_process")
    fun setprocess(text: TextView, process: String) {
        text.text = process
    }

    @JvmStatic
    @BindingAdapter("set_title")
    fun settitle(text: TextView, title: String) {
        text.text = title
    }
}