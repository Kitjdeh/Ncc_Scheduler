package com.ncc.presentation.view.main.handover

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.viewmodel.MainViewModel


class ZoomImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_image)


        // 이미지 URI 가져오기
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        // ImageView에 이미지 표시
        val imageView = findViewById<ImageView>(R.id.image_full)
        Glide.with(this).load(imageUri).into(imageView)

    }
}

//class ZoomImageFragment : BaseFragment<FragmentZoomImageBindng>(R.layout.activity_zoom_image) {
//    private val mainViewModel by activityViewModels<MainViewModel>()
//
//    override fun init() {
//        // 이미지 URI 가져오기
//        val imageUriString = intent.getStringExtra("imageUri")
//        val imageUri = Uri.parse(imageUriString)
//
//    }
//}