package com.ncc.presentation.adapter.handover.comment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncc.domain.model.DomainComment
import com.ncc.presentation.R
import com.ncc.presentation.adapter.handover.ImageHandoverRVAdapter
import com.ncc.presentation.databinding.CommentRvItemBinding
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.extension.showhorizontal

class HandoverCommentRVAdapter(
    private val viewModel: MainViewModel,
    private val context: Context
) : RecyclerView.Adapter<HandoverCommentRVAdapter.HandoverCommentRVHolder>() {
    private lateinit var imageAdatper: ZoomCommentImageRVAdapter

    inner class HandoverCommentRVHolder(val binding: CommentRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val recyclerViewCommentImage: RecyclerView = binding.commentImageRv

        fun bind(data: DomainComment) {
            binding.data = data
            binding.commentImageArea.setOnClickListener {
                var builder = AlertDialog.Builder(context)
                var layoutInflater = LayoutInflater.from(context)
                var layout = layoutInflater.inflate(R.layout.comment_image_rv, null)
                imageAdatper = ZoomCommentImageRVAdapter(data.ImageSrc, context)
                builder.setView(layout)
                layout.findViewById<RecyclerView>(R.id.comment_image_recyclerview).adapter =
                    imageAdatper
                layout.findViewById<RecyclerView>(R.id.comment_image_recyclerview)
                    .showhorizontal(context)
//                layout.findViewById<TextView>(R.id.comment_image_cnt)
//                layout.findViewById<TextView>(R.id.comment_image_cnt).text =
//                    "${data.ImageSrc!!.size}/5"
//                layout.findViewById<TextView>(R.id.comment_image_count).text =
//                    "전체 사진 수량: ${data.ImageSrc!!.size.toString()}"
                val close = DialogInterface.OnClickListener { dialog, which ->
                }

                builder.setPositiveButton("확인", close)
                builder.show()
            }

// 댓글 사진 recyclerView로 보여줌
//            binding.commentImageRv.apply {
//                adapter = ImageCommentRVAdapter(
//                    viewModel.commentList[position].ImageSrc,
//                    itemView.context
//                )
//                layoutManager = LinearLayoutManager(
//                    binding.commentImageRv.context,
//                    LinearLayoutManager.HORIZONTAL,
//                    false
//                )
////                setHasFixedSize(true)
//            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandoverCommentRVHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CommentRvItemBinding>(
            layoutInflater, R.layout.comment_rv_item, parent, false
        )
        return HandoverCommentRVHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.commentList.size
    }

    override fun onBindViewHolder(holder: HandoverCommentRVHolder, position: Int) {
        holder.bind(viewModel.commentList[position])
    }
}