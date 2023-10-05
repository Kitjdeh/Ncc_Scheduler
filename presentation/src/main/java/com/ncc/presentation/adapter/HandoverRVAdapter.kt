package com.ncc.presentation.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ncc.domain.model.DomainHandover
import com.ncc.presentation.R
import com.ncc.presentation.databinding.HandoverRvItemBinding
import com.ncc.presentation.view.main.handover.HandoverFragment
import com.ncc.presentation.viewmodel.MainViewModel

class HandoverRVAdapter(
    private val viewModel: MainViewModel,
    private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<HandoverRVAdapter.HandoverRVHolder>() {
    // Recyclerview에 없는 onclicklistner를 interface로 추가


    // 외부에서 연결해 사용할 클릭 인터페이스 생성
    private var itemClicklistener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(v: View, data: DomainHandover, position: Int)
    }

    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(listener: OnItemClickListener) {
        this.itemClicklistener = listener
    }


    inner class HandoverRVHolder(val binding: HandoverRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //        val recyclerViewCommentImage : RecyclerView = binding.
        fun bind(data: DomainHandover) {
            Log.d("어댑터확인", data.toString())
            binding.data = data
            binding.executePendingBindings()
            itemView.setOnClickListener {
                Log.d("HANDOVER_RVHOLDER", position.toString())
                itemClicklistener?.onItemClick(itemView, data, position)
            }
        }
    }

    override fun onBindViewHolder(holder: HandoverRVHolder, position: Int) {
        holder.bind(viewModel.handoverList[position])
        holder.itemView.setOnClickListener {
            Log.d("NCC item클릭", "작동")
            itemClicklistener?.onItemClick(
                holder.itemView,
                viewModel.handoverList[position],
                position
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandoverRVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HandoverRvItemBinding>(
            layoutInflater, R.layout.handover_rv_item, parent, false
        )
        return HandoverRVHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.handoverList.size
    }


}
