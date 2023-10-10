package com.ncc.presentation.view.main.handover

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.ncc.presentation.R
import com.ncc.presentation.adapter.handover.ImageHandoverRVAdapter
import com.ncc.presentation.base.BaseDialog
import com.ncc.presentation.databinding.FragmentSelectImageBinding


class SelectImageFragment :
    BaseDialog<FragmentSelectImageBinding>(R.layout.fragment_select_image) {
    private val mainViewModel by activityViewModels<com.ncc.presentation.viewmodel.MainViewModel>()
    private lateinit var imageAdatper: ImageHandoverRVAdapter
    private val maxNumber = 5
    private lateinit var registerForCommentActivityResult: ActivityResultLauncher<Intent>
    override fun init() {
        registerForCommentActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        val clipData = result.data?.clipData
                        if (clipData != null) { // 이미지를 여러 개 선택할 경우
                            val clipDataSize = clipData.itemCount
                            val selectableCount = maxNumber - mainViewModel.commentImageList.count()
                            if (clipDataSize > selectableCount) { // 최대 선택 가능한 개수를 초과해서 선택한 경우
                                shortShowToast(
                                    "이미지는 최대 ${selectableCount}장까지 첨부할 수 있습니다.",
                                )
                            } else {
                                // 선택 가능한 경우 ArrayList에 가져온 uri를 넣어준다.
                                for (i in 0 until clipDataSize) {
                                    mainViewModel.commentImageList.add(clipData.getItemAt(i).uri.toString())
                                }
                            }
                        } else { // 이미지를 한 개만 선택할 경우 null이 올 수 있다.
                            val uri = result?.data?.data
                            if (uri != null) {
                                mainViewModel.commentImageList.add(uri.toString())
                            }
                        }

                        commentImageCount()
                    }
                }
            }
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        registerForCommentActivityResult.launch(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        registerForCommentActivityResult =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                when (result.resultCode) {
//                    AppCompatActivity.RESULT_OK -> {
//                        val clipData = result.data?.clipData
//                        if (clipData != null) { // 이미지를 여러 개 선택할 경우
//                            val clipDataSize = clipData.itemCount
//                            val selectableCount = maxNumber - mainViewModel.commentImageList.count()
//                            if (clipDataSize > selectableCount) { // 최대 선택 가능한 개수를 초과해서 선택한 경우
//                                shortShowToast(
//                                    "이미지는 최대 ${selectableCount}장까지 첨부할 수 있습니다.",
//                                )
//                            } else {
//                                // 선택 가능한 경우 ArrayList에 가져온 uri를 넣어준다.
//                                for (i in 0 until clipDataSize) {
//                                    mainViewModel.commentImageList.add(clipData.getItemAt(i).uri.toString())
//                                }
//                            }
//                        } else { // 이미지를 한 개만 선택할 경우 null이 올 수 있다.
//                            val uri = result?.data?.data
//                            if (uri != null) {
//                                mainViewModel.commentImageList.add(uri.toString())
//                            }
//                        }
//
//                        commentImageCount()
//                    }
//                }
//            }

        // 나머지 코드
    }

    private fun commentImageCount() {
        val text = "${mainViewModel.commentImageList.count()}/${maxNumber}"
//        layout.findViewById<TextView>(R.id.countImage).text = text
//        val text = "${mainViewModel.commentImageList.count()}/${maxNumber}"
//        var layout = layoutInflater.inflate(R.layout.fragment_detail_handover, null)
//        layout.findViewById<TextView>(R.id.countImage).text = text
//
        shortShowToast("현재 선택한 사진 ${text}")
    }

}