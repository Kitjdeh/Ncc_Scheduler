package com.ncc.presentation.view.admin

import android.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.ncc.presentation.R
import com.ncc.presentation.adapter.admin.AdminRoutineRVAdapter
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentAdminBinding
import com.ncc.presentation.viewmodel.AdminViewModel
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.extension.showVertical
import com.ncc.presentation.widget.utils.Organization


class AdminFragment : BaseFragment<FragmentAdminBinding>(R.layout.fragment_admin) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val adminViewModel by activityViewModels<AdminViewModel>()
    override fun init() {
        selectShift()
        selectWork()
        observe()
        adminViewModel.getRoutineList()

    }

    private fun observe() {
        adminViewModel.getRoutineEvent.observe(this) {
            binding.adminRoutine.adapter = AdminRoutineRVAdapter(adminViewModel)
            binding.adminRoutine.showVertical(requireContext())
        }
    }

    private fun selectWork() {
        val workList = listOf("전체 보기", "Daily", "Weekly", "Monthly")
//        val workList = Organization.work.toTypedArray()
        val workSpinner = binding.selectWork
        val shift = adminViewModel.selectedShift
        var selectedType = "선택해주세요"
        val workSpinnerAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                workList
            )
        workSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        workSpinner.adapter = workSpinnerAdapter
        workSpinner.setSelection(0)
        workSpinner.firstVisiblePosition
        workSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedType = workList[position]
                adminViewModel.filteringTypeRoutineList(selectedType)
//                adminViewModel.filteringRoutine(shift, selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedType = workList.first()
                adminViewModel.filteringTypeRoutineList(selectedType)
//                adminViewModel.filteringRoutine(shift, selectedType)
            }
        }
    }

    private fun selectShift() {
        val shiftList = listOf("전체 보기", "EVE", "NIG", "MOR")
        val shiftSpinner = binding.selectShift
        var selectShift = "선택해주세요"
        val type = adminViewModel.selectedType
        val shiftSpinnerAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                shiftList
            )
        shiftSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        shiftSpinner.adapter = shiftSpinnerAdapter
        shiftSpinner.setSelection(0)
        shiftSpinner.firstVisiblePosition
        shiftSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectShift = shiftList[position]
                adminViewModel.filteringShiftRoutineList(selectShift)
//                adminViewModel.filteringRoutine(selectShift, type)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectShift = shiftList.first()
//                adminViewModel.filteringRoutine(selectShift, type)

                adminViewModel.filteringShiftRoutineList(selectShift)
            }
        }
    }

    private fun writeRoutine() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Routine 업무 등록")
        builder.setIcon(R.drawable.ncc_main_logo)
    }
}