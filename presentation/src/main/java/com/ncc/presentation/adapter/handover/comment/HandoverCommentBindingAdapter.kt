package com.ncc.presentation.adapter.handover.comment

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object HandoverCommentBindingAdapter {
    @JvmStatic
    @BindingAdapter("set_time")
    fun settime(text: TextView, time: String) {
//        val endIndex = time.length
//        val startIndex = endIndex - 8
//        text.text = time.substring(startIndex, endIndex)
        text.text = time
    }

    @JvmStatic
    @BindingAdapter("set_name")
    fun setname(text: TextView, name: String) {
        text.text = name
    }

    @JvmStatic
    @BindingAdapter("set_content")
    fun setcontent(text: TextView, content: String) {
        text.text = content
    }

    @JvmStatic
    @BindingAdapter("set_image")
    fun setImg(img: ImageView, imageSrc: List<String>) {
        if (imageSrc.isNotEmpty()) {

//            Log.d("NCC set_img", imageSrc.toString())
            Glide.with(img).load(Uri.parse(imageSrc[0])).into(img)
        } else {
            img.visibility = View.INVISIBLE
        }

    }
}
