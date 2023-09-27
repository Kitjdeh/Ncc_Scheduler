package com.ncc.presentation.view.main.handover

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ncc.domain.model.DomainComment
import com.ncc.domain.model.DomainHandover
import com.ncc.presentation.R
import com.ncc.presentation.adapter.HandoverCommentRVAdapter
import com.ncc.presentation.adapter.HandoverRVAdapter
import com.ncc.presentation.adapter.ImageHandoverRVAdapter
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentHandoverBinding
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.extension.showVertical
import com.ncc.presentation.widget.extension.showhorizontal
import com.ncc.presentation.widget.utils.Organization
import com.ncc.presentation.widget.utils.ProcessList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HandoverFragment :
    BaseFragment<FragmentHandoverBinding>(R.layout.fragment_handover) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var imageAdatper: ImageHandoverRVAdapter
    override fun init() {
        viewPosition()
        binding.fragment = this
        mainViewModel.getHandover(mainViewModel.selectedDate, mainViewModel.position)
        observeViewModel()
        binding.WriteHandoverBtn.setOnClickListener {
            if (mainViewModel.position == "전체보기") {
                shortShowToast("팀을 선택해 주세요")
            } else {
                clickWriteBtn()
            }
        }
        binding.handoverRv.setOnClickListener { }
        imageAdatper = ImageHandoverRVAdapter(mainViewModel, requireContext())
    }

    private val maxNumber = 5
    private fun nowTime(): String =
        SimpleDateFormat("yyyy-MM-dd일-HH-mm", Locale("ko", "KR")).format(
            Date(System.currentTimeMillis())
        )

    fun clickWriteBtn() {
        mainViewModel.imageClear()
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("인수인계 등록")
        builder.setIcon(R.drawable.ncc_main_logo)
        var layout = layoutInflater.inflate(R.layout.fragment_write_handover, null)
        builder.setView(layout)
        mainViewModel.selectedDate
        layout.findViewById<TextView>(R.id.user_position).text =
            "Postion : ${mainViewModel.position}"
        imageAdatper = ImageHandoverRVAdapter(mainViewModel, requireContext())
        layout.findViewById<RecyclerView>(R.id.writeHandoverImageRecylerView).adapter = imageAdatper
        layout.findViewById<RecyclerView>(R.id.writeHandoverImageRecylerView)
            .showhorizontal(requireContext())
        var selectedProcess = ""
        val processList = ProcessList.items.toTypedArray()
        val spinner = layout.findViewById<Spinner>(R.id.processSpinner)
        val spinneradapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                processList
            )
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinneradapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedProcess = processList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedProcess = processList.first()
            }
        }

        layout.findViewById<AppCompatButton>(R.id.handoverImageUploadBtn).setOnClickListener {
            if (mainViewModel.imageList.count() >= maxNumber) {
                shortShowToast(
                    "이미지는 최대 ${maxNumber}장까지 첨부할 수 있습니다.",
                )
                return@setOnClickListener
            }
            shortShowToast(
                "${mainViewModel.imageList.size}",
            )
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            registerForHandoverActivityResult.launch(intent)
        }

        val listener = DialogInterface.OnClickListener { dialog, which ->
            val alert = dialog as AlertDialog
            val title = alert.findViewById<EditText>(R.id.title_edit).text.toString()
//            val routine = alert.findViewById<EditText>(R.id.routine_edit).text.toString()
            val routine = selectedProcess
            val content = alert.findViewById<EditText>(R.id.content_edit).text.toString()
            val name = mainViewModel.user
            val date = mainViewModel.selectedDate
            val time = nowTime()
            mainViewModel.setHandover(
                name,
                "",
                routine,
                title,
                date,
                time,
                content,
                mainViewModel.imageList, mainViewModel.position
            )
        }
        val cancelwrite = DialogInterface.OnClickListener { dialog, which ->
            mainViewModel.imageList.clear()
        }
        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", cancelwrite)
        builder.show()
    }

    private fun observeViewModel() {
//        binding.userPosition.text = mainViewModel.position
        mainViewModel.selectedDateEvent.observe(this) {
            initRecyclerView()
        }
        mainViewModel.getHandovereEvent.observe(this) {
            initRecyclerView()
        }
//        mainViewModel.getUserInfoEvent.observe(this, Observer { user ->
////            Log.d("유저정보position적용", user.toString())
//            binding.userPosition.text = user.position
//        })
    }


    private fun showDetailDialog(data: DomainHandover) {
        val detailHandoverFragment = DetailHandoverFragment.newInstance(data)
        detailHandoverFragment.showFragment(requireFragmentManager())
    }

    private fun viewPosition() {
        val processList = Organization.viewPosition.toTypedArray()
        val spinner = binding.viewPosition
        var selectPosition = mainViewModel.position
        val index = processList.indexOf(selectPosition)
        val spinneradapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                processList
            )
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinneradapter
        spinner.setSelection(index)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectPosition = processList[position]
                mainViewModel.viewPosition(selectPosition)
//                mainViewModel.getHandover(mainViewModel.selectedDate, selectPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                selectPosition = processList.first()
            }
        }
    }

    private fun initRecyclerView() {
        var handoverAdapter = HandoverRVAdapter(mainViewModel, requireActivity())

        binding.handoverRv.adapter = handoverAdapter
        binding.handoverRv.showVertical(requireContext())
        handoverAdapter.setItemClickListener(object : HandoverRVAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: DomainHandover, position: Int) {
//                showDetailDialog(data)
                handoverDetail(data)
            }
        })
    }

    fun handoverDetail(data: DomainHandover) {
        mainViewModel.getComment(data)
        mainViewModel.getHandoverImage(data)
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("인수인계 사항")
        builder.setIcon(R.drawable.ncc_main_logo)
        var layout = layoutInflater.inflate(R.layout.fragment_detail_handover, null)
        builder.setView(layout)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_round)


        imageAdatper = ImageHandoverRVAdapter(mainViewModel, requireContext())
        layout.findViewById<RecyclerView>(R.id.comment_Rv).adapter =
            HandoverCommentRVAdapter(mainViewModel)
        layout.findViewById<RecyclerView>(R.id.comment_Rv).showVertical(requireContext())
        layout.findViewById<RecyclerView>(R.id.handover_photo_recyclerview).adapter = imageAdatper
        layout.findViewById<RecyclerView>(R.id.handover_photo_recyclerview)
            .showhorizontal(requireContext())
        layout.findViewById<TextView>(R.id.handoverUser).text = "${data.name}"
        layout.findViewById<TextView>(R.id.handoverTeam).text =
            data.position
//            if (mainViewModel.selectedPosition == "manager") "교대대리" else mainViewModel.selectedPosition
        layout.findViewById<TextView>(R.id.handoverTime).text = data.time.substring(2)
        layout.findViewById<TextView>(R.id.handoverContent).text = data.content
        layout.findViewById<TextView>(R.id.handoverRoutine).text = data.routine
        layout.findViewById<TextView>(R.id.handoverTitle).text = data.title

        layout.findViewById<TextView>(R.id.handover_photo_count).text =
            "${mainViewModel.imageList.size}/5"

        layout.findViewById<ImageButton>(R.id.handoverCommentImageBtn).setOnClickListener {
            Log.d("댓글사진등록 버튼 클릭", "")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            registerForCommentActivityResult.launch(intent)
        }
        layout.findViewById<AppCompatButton>(R.id.handoverCommentWriteBtn).setOnClickListener {
            var content = layout.findViewById<EditText>(R.id.content_edit).text.toString()
            val newComment = DomainComment(
                mainViewModel.user,
                data.id,
                mainViewModel.userUid,
                content,
                mainViewModel.commentImageList,
                data.date,
                nowTime(),
            )
            if (content.isNotEmpty()) {
                mainViewModel.setComment(
                    mainViewModel.user,
                    data.id,
                    mainViewModel.userUid,
                    content,
                    mainViewModel.commentImageList,
                    data.date,
                    nowTime(),
                    data.position,
                )
//                mainViewModel.commentList.add(newComment)
                Log.d("댓글등록", "${newComment.toString()}")
//                shortShowToast("댓글이 등록되었습니다.")
                mainViewModel.getHandover(data.date, mainViewModel.position)
                layout.findViewById<EditText>(R.id.content_edit).text.clear()
            }
        }
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "닫기") { _, _ -> }
        alertDialog.show()
//        builder.setPositiveButton("댓글 등록", listener)
//        builder.setNegativeButton("닫기", null)
//        builder.show()
    }

    private val registerForHandoverActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val clipData = result.data?.clipData
                    if (clipData != null) { // 이미지를 여러 개 선택할 경우
                        val clipDataSize = clipData.itemCount
                        val selectableCount = maxNumber - mainViewModel.imageList.count()
                        if (clipDataSize > selectableCount) { // 최대 선택 가능한 개수를 초과해서 선택한 경우
                            shortShowToast(
                                "이미지는 최대 ${selectableCount}장까지 첨부할 수 있습니다.",
                            )
                        } else {
                            // 선택 가능한 경우 ArrayList에 가져온 uri를 넣어준다.
                            for (i in 0 until clipDataSize) {
                                mainViewModel.imageList.add(clipData.getItemAt(i).uri.toString())
                            }
                        }
                    } else { // 이미지를 한 개만 선택할 경우 null이 올 수 있다.
                        val uri = result?.data?.data
                        if (uri != null) {
                            mainViewModel.imageList.add(uri.toString())
                        }
                    }
                    Log.d("NCCimageclick", mainViewModel.imageList.toString())
                    imageAdatper.notifyDataSetChanged()
                    // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
                    printCount()
                }
            }
        }
    private val registerForCommentActivityResult =
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

    private fun printCount() {
        val text = "${mainViewModel.imageList.count()}/${maxNumber}"
        var layout = layoutInflater.inflate(R.layout.fragment_detail_handover, null)
        layout.findViewById<TextView>(R.id.handover_photo_count).text = text
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

