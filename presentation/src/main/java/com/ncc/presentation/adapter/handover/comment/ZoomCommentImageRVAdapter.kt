package com.ncc.presentation.adapter.handover.comment

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.ncc.presentation.R
import com.ncc.presentation.databinding.CommentImageRvItemBinding
import com.ncc.presentation.databinding.CommentZoomImageRvItemBinding

class ZoomCommentImageRVAdapter(
    private val imgSrcList: List<String>?,
    private val context: Context
) : RecyclerView.Adapter<ZoomCommentImageRVAdapter.ZoomCommentImageRVHolder>() {

    inner class ZoomCommentImageRVHolder(val binding: CommentZoomImageRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String?) {
            val photoview = binding.commentImageRvItem
            Glide.with(binding.root).load(Uri.parse(item)).into(photoview)
            binding.commentImageNumber.text = "${position + 1}/${imgSrcList!!.size.toString()}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ZoomCommentImageRVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CommentZoomImageRvItemBinding>(
            layoutInflater, R.layout.comment_zoom_image_rv_item, parent, false
        )
        return ZoomCommentImageRVHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ZoomCommentImageRVAdapter.ZoomCommentImageRVHolder,
        position: Int
    ) {
        holder.bind(imgSrcList?.get(position))
    }

    override fun getItemCount(): Int {
        return imgSrcList!!.size
    }
}