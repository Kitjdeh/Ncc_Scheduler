package com.ncc.presentation.adapter.admin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ncc.domain.model.DomainRoutine
import com.ncc.presentation.R
import com.ncc.presentation.adapter.routine.RoutineRVAdapter
import com.ncc.presentation.databinding.AdminRoutineRvItemBinding
import com.ncc.presentation.databinding.RoutineRvItemBinding
import com.ncc.presentation.viewmodel.AdminViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminRoutineRVAdapter(
    private val viewModel: AdminViewModel
) : RecyclerView.Adapter<AdminRoutineRVAdapter.RoutineRVHolder>() {

    inner class RoutineRVHolder(val binding: AdminRoutineRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DomainRoutine) {
            binding.data = data
            binding.executePendingBindings()
            binding.delteBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteRoutine(data.id)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineRVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AdminRoutineRvItemBinding>(
            layoutInflater, R.layout.admin_routine_rv_item, parent, false
        )
        return RoutineRVHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.getRoutineEvent.value!!.size
    }

    override fun onBindViewHolder(holder: RoutineRVHolder, position: Int) {
        holder.bind(viewModel.getRoutineEvent.value!![position])
    }
}