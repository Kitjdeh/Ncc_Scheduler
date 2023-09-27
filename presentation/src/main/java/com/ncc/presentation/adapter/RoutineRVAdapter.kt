package com.ncc.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ncc.domain.model.DomainRoutine
import com.ncc.presentation.R
import com.ncc.presentation.databinding.RoutineRvItemBinding
import com.ncc.presentation.viewmodel.MainViewModel

class RoutineRVAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<RoutineRVAdapter.RoutineRVHolder>() {


    inner class RoutineRVHolder(val binding: RoutineRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DomainRoutine) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineRVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RoutineRvItemBinding>(
            layoutInflater, R.layout.routine_rv_item, parent, false
        )
        return RoutineRVHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.routineList.size
    }

    override fun onBindViewHolder(holder: RoutineRVHolder, position: Int) {
        holder.bind(viewModel.routineList[position])
    }
}