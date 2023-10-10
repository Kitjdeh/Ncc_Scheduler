package com.ncc.presentation.view.main.handover

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.ncc.domain.model.DomainHandover
import com.ncc.presentation.R
import com.ncc.presentation.adapter.handover.comment.HandoverCommentRVAdapter
import com.ncc.presentation.adapter.handover.ImageHandoverRVAdapter
import com.ncc.presentation.base.BaseDialog
import com.ncc.presentation.databinding.FragmentDetailHandoverBinding
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.extension.showVertical
import com.ncc.presentation.widget.extension.showhorizontal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailHandoverFragment(data: DomainHandover) :
    BaseDialog<FragmentDetailHandoverBinding>(R.layout.fragment_detail_handover) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var imageAdatper: ImageHandoverRVAdapter
    private var data = data
    private lateinit var registerForCommentActivityResult: ActivityResultLauncher<Intent>
    override fun init() {
        handoverDetail(data)
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
//                    Log.d("NCCimageclick", mainViewModel.commentImageList.toString())
//                    imageAdatper.notifyDataSetChanged()
                        // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
//                    printCount()
                    }
                }
            }
    }

    companion object {
        fun newInstance(data: DomainHandover): DetailHandoverFragment {
            val fragment = DetailHandoverFragment(data)
            return fragment
        }
    }

    fun showFragment(manager: FragmentManager) {
        show(manager, "detail_handover_dialog")
    }

    fun getInstance(): DetailHandoverFragment {
        return DetailHandoverFragment(data)
    }

    fun onClick(p0: View?) {
        dismiss()
    }

    private val maxNumber = 5


    private fun handoverDetail(data: DomainHandover) {
        mainViewModel.getComment(data)
        mainViewModel.getHandoverImage(data)
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("인수인계 사항")
        builder.setIcon(R.drawable.ncc_main_logo)
        var layout = layoutInflater.inflate(R.layout.fragment_detail_handover, null)
        builder.setView(layout)
        imageAdatper = ImageHandoverRVAdapter(mainViewModel, requireContext())
        layout.findViewById<RecyclerView>(R.id.comment_Rv).adapter =
            HandoverCommentRVAdapter(mainViewModel)
        layout.findViewById<RecyclerView>(R.id.comment_Rv).showVertical(requireContext())
        layout.findViewById<RecyclerView>(R.id.handover_photo_recyclerview).adapter = imageAdatper
        layout.findViewById<RecyclerView>(R.id.handover_photo_recyclerview)
            .showhorizontal(requireContext())
        layout.findViewById<TextView>(R.id.handoverUser).text = "${data.name}"
        layout.findViewById<TextView>(R.id.handoverTeam).text =
            if (mainViewModel.position == "manager") "교대대리" else mainViewModel.position

        layout.findViewById<TextView>(R.id.handoverTime).text = data.time.substring(2)
        layout.findViewById<TextView>(R.id.handoverContent).text = data.content
        layout.findViewById<TextView>(R.id.handoverRoutine).text = data.routine
        layout.findViewById<TextView>(R.id.handoverTitle).text = data.title

        layout.findViewById<TextView>(R.id.handover_photo_count).text =
            "${mainViewModel.imageList.size}/5"

        layout.findViewById<ImageButton>(R.id.handoverCommentImageBtn).setOnClickListener {
            Log.d("댓글사진등록 버튼 클릭", "")
            val selectImageFragment = SelectImageFragment()
            selectImageFragment.show(requireFragmentManager(), "select_image_dialog")
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            registerForCommentActivityResult.launch(intent)
//            registerForCommentActivityResult.launch(intent)
//            val SelectImageFragment = SelectImageFragment()

        }
        layout.findViewById<AppCompatButton>(R.id.handoverCommentWriteBtn).setOnClickListener {
            var content = layout.findViewById<EditText>(R.id.content_edit).text.toString()
            var handoverid = data.id
            Log.d("댓글입력시작", mainViewModel.commentImageList.toString())
            if (content.length > 0) {
                mainViewModel.setComment(
                    mainViewModel.user,
                    handoverid,
                    "",
                    content,
                    mainViewModel.commentImageList,
                    data.date,
                    nowTime(), data.position,
                )
//                mainViewModel.getHandover(data.date)
            }
        }

        var listener = DialogInterface.OnClickListener { dialog, which ->
            var alert = dialog as AlertDialog
            var content = alert.findViewById<EditText>(R.id.content_edit).text.toString()
            var handoverid = data.id
            Log.d("댓글입력시작", mainViewModel.commentImageList.toString())
            if (content.length > 0) {
                mainViewModel.setComment(
                    mainViewModel.user,
                    handoverid,
                    "",
                    content,
                    mainViewModel.commentImageList,
                    data.date,
                    nowTime(), data.position,
                )
            }
        }

        builder.setNegativeButton("닫기", null)
        builder.show()
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

    private fun nowTime(): String =
        SimpleDateFormat("yyyy-MM-dd일-HH-mm", Locale("ko", "KR")).format(
            Date(System.currentTimeMillis())
        )


}