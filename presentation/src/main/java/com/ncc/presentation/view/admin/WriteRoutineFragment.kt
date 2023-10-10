package com.ncc.presentation.view.admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentWriteRoutineBinding
import com.ncc.presentation.viewmodel.AdminViewModel
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.utils.Organization
import java.time.LocalDate


class WriteRoutineFragment :
    BaseFragment<FragmentWriteRoutineBinding>(R.layout.fragment_write_routine) {
    private val adminViewModel by activityViewModels<AdminViewModel>()
    override fun init() {
        selectShift()
        selectWork()
        binding.settingRoutine.setOnClickListener {
            Log.d("뷰모델 type확인", adminViewModel.type)
            if (adminViewModel.type == "Monthly") {
                selectMonthFirst()
            } else if (adminViewModel.type == "Weekly") {
                selectWeekFirst()
            } else if (adminViewModel.type == "Daily") {
            } else {
                shortShowToast("업무 타입 선택을 해주세요")
            }
        }
        initObserve()
        binding.setRoutineBtn.setOnClickListener {
            val title = binding.titleEdit.text.toString()
            val content = binding.contentEdit.text.toString()
            Log.d("team값", adminViewModel.team)
            Log.d("title값", title)
            Log.d("date값", adminViewModel.date)
            Log.d("month값", adminViewModel.month.toString())
            Log.d("week값", adminViewModel.week.toString())
            adminViewModel.setRoutine(title, content)
            setRoutine()
        }
    }

    private fun setRoutine() {

    }


    private fun initObserve() {
        adminViewModel.changeTypeEvent.observe(this) { type ->

            if (type == "Monthly") {
                binding.firstLine.visibility = View.VISIBLE
                binding.secondLine.visibility = View.VISIBLE
                binding.thirdLine.visibility = View.VISIBLE
                binding.routineType.text = "선택 월"
            } else if (type == "Weekly") {
                binding.secondLine.visibility = View.VISIBLE
                binding.thirdLine.visibility = View.VISIBLE
                binding.firstLine.visibility = View.INVISIBLE
                binding.routineType.text = "선택 주차"
            } else if (type == "Daily") {
                binding.firstLine.visibility = View.VISIBLE
                binding.routineType.text = "매일"
                binding.secondLine.visibility = View.INVISIBLE
                binding.thirdLine.visibility = View.INVISIBLE
                binding.fourthLine.visibility = View.INVISIBLE
            }
        }
        adminViewModel.monthEvent.observe(this) { listMonth ->
            binding.first.text = listMonth.toString()
        }
        adminViewModel.weekEvent.observe(this) { listWeek ->
            Log.d("위크toList", listWeek.toList().toString())
            binding.secondLine.visibility = View.VISIBLE
            Log.d("위크", listWeek.toString())
            binding.second.text = listWeek.toString()
        }
        adminViewModel.dayOfMonthEvent.observe(this) { dayOfMonth ->
            binding.third.text = dayOfMonth.toString()
        }
        adminViewModel.dayOfWeekEvent.observe(this) { dayOfWeek ->
            binding.third.text = dayOfWeek.toString()
        }
    }

    private fun selectRoutineDate() {
        if (adminViewModel.type == "Daily") {
            shortShowToast("Daily는 일정 항목 없음")
        } else if (adminViewModel.type == "Weekly") {

        } else if (adminViewModel.type == "Monthly") {
        }
    }

    private fun selectMonthFirst() {
        val selectedMonth = mutableListOf<String>()
        val checkedItems = BooleanArray(13)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("월 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        var choice = "매월"
        val items = arrayOf("매월", "월 선택")
        builder.setSingleChoiceItems(items, 0) { dialog, which ->
            choice = items[which]
        }
        builder.setPositiveButton("확인") { _, _ ->
            if (choice == "매월") {
                val items = arrayListOf<String>(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12"
                )
                adminViewModel.selectMonth(items)
                selectMonthCheckType()
            } else {
                selectMonthSecond()
            }
        }
// 다이얼로그를 생성하고 표시합니다.
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectMonthSecond() {
        val selectedMonth = mutableListOf<String>()
        val checkedItems = BooleanArray(13)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("월 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        val items = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            if (which == 0) {
                checkedItems[0] = !checkedItems[0]
                for (i in items.indices) {
                    checkedItems[i] = isChecked
                }
            } else {
                checkedItems[which] = isChecked
            }
            adminViewModel.checkMonthSelected(checkedItems)
        }
        builder.setPositiveButton("확인") { _, _ ->
            // 체크된 아이템을 처리합니다.
            for (i in items.indices) {
                if (checkedItems[i] && i != 0) {
                    selectedMonth.add(items[i])
                }
            }
            adminViewModel.selectMonth(selectedMonth)
            selectMonthCheckType()
        }
        val dialog = builder.create()
        var positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        dialog.setOnShowListener { dialogInterface ->
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.isEnabled = false
        }
        adminViewModel.selectedMonthEvent.observe(this) {
            if (positiveButton != null) {
                positiveButton.isEnabled = checkedItems.any { it }
            }
        }
        dialog.show()
    }

    private fun selectMonthCheckType() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("월 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        var choice = "주 단위"
        val items = arrayOf("주 단위", "일 단위")
        builder.setSingleChoiceItems(items, 0) { dialog, which ->
            choice = items[which]
        }
        builder.setPositiveButton("확인") { _, _ ->
            if (choice == "주 단위") {
                selectWeekFirst()
                binding.thirdLineName.text = "요일"
            } else {
                binding.secondLine.visibility = View.INVISIBLE
                binding.thirdLineName.text = "선택 일"
                selectDayOfMonth()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectDayOfMonth() {
        val selectedDayOfMonth = mutableListOf<String>()
        val checkedItems = BooleanArray(31)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Day 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        val items = (1..31).map { it.toString() }.toTypedArray()
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            checkedItems[which] = isChecked
//            if (which == 0) {
//                checkedItems[0] = !checkedItems[0]
//                for (i in items.indices) {
//                    checkedItems[i] = isChecked
//                }
//            } else {
//                checkedItems[which] = isChecked
//            }
            adminViewModel.checkDayOfMonthSelected(checkedItems)

        }
        builder.setPositiveButton("확인") { _, _ ->
            // 체크된 아이템을 처리합니다.
            for (i in items.indices) {
                if (checkedItems[i] && i != 0) {
                    selectedDayOfMonth.add(items[i])
                }
            }
            adminViewModel.selectDayOfMonth(selectedDayOfMonth)
        }
        val dialog = builder.create()
        var positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        dialog.setOnShowListener { dialogInterface ->
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.isEnabled = false
        }
        adminViewModel.selectedDayOfMonthEvent.observe(this) {
            if (positiveButton != null) {
                positiveButton.isEnabled = checkedItems.any { it }
            }
        }
        dialog.show()
    }

    private fun selectDayOfWeek() {
        val selectedDayOfWeek = mutableListOf<String>()
        val checkedItems = BooleanArray(7)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("요일 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        val items = arrayOf("일", "월", "화", "수", "목", "금", "토")
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            checkedItems[which] = isChecked
            adminViewModel.checkDayOfWeekSelected(checkedItems)
        }
        builder.setPositiveButton("확인") { _, _ ->
            // 체크된 아이템을 처리합니다.
            for (i in items.indices) {
                if (checkedItems[i] && i != 0) {
                    selectedDayOfWeek.add(items[i])
                }
            }
            adminViewModel.selectDayOfWeek(selectedDayOfWeek)
//            selectMonthCheckType()
        }
        val dialog = builder.create()
        var positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        dialog.setOnShowListener { dialogInterface ->
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.isEnabled = false
        }

        adminViewModel.selectedDayOfWeekEvent.observe(this) {
            if (positiveButton != null) {
                positiveButton.isEnabled = checkedItems.any { it }
            }

//            positiveButton.isEnabled = selectedDayOfWeek.isNotEmpty()
        }
        dialog.show()
    }

    private fun selectWeekFirst() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("주간 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        var choice = "매주"
        val items = arrayOf("매주", "주 선택")
        builder.setSingleChoiceItems(items, 0) { dialog, which ->
            choice = items[which]
        }
        builder.setPositiveButton("확인") { _, _ ->
            if (choice == "매주") {
                val items = arrayListOf<String>("1", "2", "3", "4", "5")
                adminViewModel.selectWeek(items)
                selectDayOfWeek()
            } else {
                selectWeekSecond()
            }
        }
// 다이얼로그를 생성하고 표시합니다.
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectWeekSecond() {
        val selectedWeek = mutableListOf<String>()
        val checkedItems = BooleanArray(5)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("주간 선택")
        builder.setIcon(R.drawable.ncc_main_logo)
        val items = arrayOf("1", "2", "3", "4", "5")
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            if (which == 0) {
                checkedItems[0] = !checkedItems[0]
                for (i in items.indices) {
                    checkedItems[i] = isChecked
                }
            } else {
                checkedItems[which] = isChecked
            }
            adminViewModel.checkWeekSelected(checkedItems)
        }
        builder.setPositiveButton("확인") { _, _ ->
            // 체크된 아이템을 처리합니다.
            for (i in items.indices) {
                if (checkedItems[i] && i != 0) {
                    selectedWeek.add(items[i])
                }
            }
            adminViewModel.selectWeek(selectedWeek)
            selectDayOfWeek()
        }
        val dialog = builder.create()
        var positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        dialog.setOnShowListener { dialogInterface ->
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//            positiveButton.isEnabled = checkedItems.any { it == true }
            positiveButton.isEnabled = false
        }
        adminViewModel.selectedWeekEvent.observe(this) {
            if (positiveButton != null) {
                positiveButton.isEnabled = checkedItems.any { it }
            }

        }
        dialog.show()
    }


    private fun selectWork() {
        val workList = Organization.work.toTypedArray()
        val workSpinner = binding.selectWork
        var selectWork = "선택해주세요"
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
                selectWork = workList[position]
                adminViewModel.changeWork(selectWork)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectWork = workList.first()
                adminViewModel.changeWork(selectWork)
            }
        }
    }

    private fun selectShift() {
        val shiftList = Organization.shift.toTypedArray()
        val shiftSpinner = binding.selectShift
        var selectShift = "선택해주세요"
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
                adminViewModel.changeShift(selectShift)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectShift = shiftList.first()
                adminViewModel.changeShift(selectShift)
            }
        }
    }

}