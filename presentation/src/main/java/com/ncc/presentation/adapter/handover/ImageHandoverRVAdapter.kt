package com.ncc.presentation.adapter.handover

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.ncc.presentation.R
import com.ncc.presentation.databinding.ImageRvItemBinding
import com.ncc.presentation.view.main.handover.ZoomImageActivity
import com.ncc.presentation.viewmodel.MainViewModel

class ImageHandoverRVAdapter(
    private val viewModel: MainViewModel,
//    private val imageClickCallback: (String) -> Unit
    private val context: Context
) :
    RecyclerView.Adapter<ImageHandoverRVAdapter.ImageHandoverRVHolder>() {

    inner class ImageHandoverRVHolder(val binding: ImageRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            Glide.with(binding.root).load(Uri.parse(item)).into(binding.imageArea)
            binding.imageArea.setOnClickListener {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageHandoverRVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ImageRvItemBinding>(
            layoutInflater, R.layout.image_rv_item, parent, false
        )
        return ImageHandoverRVHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImageHandoverRVHolder,
        position: Int
    ) {
        holder.bind(viewModel.imageList[position])
    }

    override fun getItemCount(): Int {
        return viewModel.imageList.size
    }

    private fun clickEvent(imageUri: Uri) {
        val intent = Intent(context, ZoomImageActivity::class.java)
        intent.putExtra("imageUri", imageUri.toString())
        context.startActivity(intent)
    }
}