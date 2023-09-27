package com.ncc.presentation.adapter

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.ncc.presentation.R
import com.ncc.presentation.databinding.CommentImageRvItemBinding
import com.ncc.presentation.databinding.CommentRvItemBinding
import com.ncc.presentation.viewmodel.MainViewModel

class ImageCommentRVAdapter(
    private val imgSrcList: List<String>?,
    private val context: Context
) : RecyclerView.Adapter<ImageCommentRVAdapter.ImageCommentRVHolder>() {

    inner class ImageCommentRVHolder(val binding: CommentImageRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            Glide.with(binding.root).load(Uri.parse(item)).into(binding.commentImageArea)
            binding.commentImageArea.setOnClickListener {
                var builder = AlertDialog.Builder(context)
                // LayoutInflater 객체 가져오기
                val layoutInflater = LayoutInflater.from(context)
                // 원하는 레이아웃을 가져오기
                val layout = layoutInflater.inflate(R.layout.fragment_zoom_image, null)
                builder.setView(layout)
                val photoview = layout.findViewById<PhotoView>(R.id.image_full)
                Glide.with(context).load(Uri.parse(item))
                    .into(photoview)
                val close = DialogInterface.OnClickListener { dialog, which ->
                }
                builder.setPositiveButton("확인", close)
                builder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCommentRVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CommentImageRvItemBinding>(
            layoutInflater, R.layout.comment_image_rv_item, parent, false
        )
        return ImageCommentRVHolder(binding)
    }

    override fun getItemCount(): Int {

        return imgSrcList!!.size
    }

    override fun onBindViewHolder(holder: ImageCommentRVHolder, position: Int) {
        holder.bind(imgSrcList!![position])
    }


}