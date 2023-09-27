package com.ncc.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncc.domain.model.DomainComment
import com.ncc.presentation.R
import com.ncc.presentation.databinding.CommentRvItemBinding
import com.ncc.presentation.viewmodel.MainViewModel

class HandoverCommentRVAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<HandoverCommentRVAdapter.HandoverCommentRVHolder>() {
    inner class HandoverCommentRVHolder(val binding: CommentRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val recyclerViewCommentImage: RecyclerView = binding.commentImageRv

        fun bind(data: DomainComment) {
            binding.data = data
            binding.executePendingBindings()
            binding.commentImageRv.apply {
                Log.d("NCCComment", viewModel.commentImageList.toString())
                adapter = ImageCommentRVAdapter(
                    viewModel.commentList[position].ImageSrc,
                    itemView.context
                )
                layoutManager = LinearLayoutManager(
                    binding.commentImageRv.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                setHasFixedSize(true)
            }
//            imageCommentAdapter =
//                ImageCommentRVAdapter(viewModel.commentImageList, itemView.context)
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