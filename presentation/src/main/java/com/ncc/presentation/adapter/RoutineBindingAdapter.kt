package com.ncc.presentation.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

object RoutineBindingAdapter {
    @JvmStatic
    @BindingAdapter("set_process")
    fun setprocess(text: TextView, process:String){
        text.text = process
    }
    @JvmStatic
    @BindingAdapter("set_title")
    fun settitle(text: TextView, title:String){
        text.text = title
    }
    @JvmStatic
    @BindingAdapter("set_team")
    fun setteam(text: TextView, team:String){
        text.text = team
    }
}