package com.ncc.presentation.view.main.routine

import androidx.fragment.app.activityViewModels
import com.ncc.presentation.R
import com.ncc.presentation.adapter.routine.RoutineRVAdapter
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentRoutineBinding
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.extension.showVertical
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class RoutineFragment :
    BaseFragment<FragmentRoutineBinding>(R.layout.fragment_routine) {

    private val mainViewModel by activityViewModels<MainViewModel>()
    val formatter = DateTimeFormatter.ofPattern("YYYY년 MM월 d일", Locale.getDefault())

    override fun init() {
        binding.fragment = this
        observeViewModel()
        binding.selectedDate.text = mainViewModel.selectedDate
    }

    private fun observeViewModel() {
        mainViewModel.getRoutineEvent.observe(this) {
            initRecyclerView()
            binding.selectedDate.text = formatDate(mainViewModel.selectedDate)
        }
    }

    private fun initRecyclerView() {
        binding.routineRv.adapter = RoutineRVAdapter(mainViewModel)
        binding.routineRv.showVertical(requireContext())
    }

    fun formatDate(inputDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("YYYY년 MM월 dd일")
        val date = LocalDate.parse(inputDate, DateTimeFormatter.BASIC_ISO_DATE)
        return date.format(formatter)
    }
}