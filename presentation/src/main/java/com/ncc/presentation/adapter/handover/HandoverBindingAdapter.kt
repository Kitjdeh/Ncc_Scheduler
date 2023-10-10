package com.ncc.presentation.adapter.handover

import android.widget.TextView
import androidx.databinding.BindingAdapter

object HandoverBindingAdapter {

    @JvmStatic
    @BindingAdapter("set_time")
    fun settime(text: TextView, time: String) {
//        val endIndex = time.length
//        val startIndex = endIndex - 8
//        text.text = time.substring(startIndex,endIndex)
        text.text = time
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