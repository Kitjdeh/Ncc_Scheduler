package com.ncc.presentation.view.main.calendar


import android.util.Log
import androidx.fragment.app.activityViewModels
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentCalendarBinding
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.widget.utils.Organization
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate


class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun init() {
        getdata()
        startCalendar()
        observeViewModel()
        schedule()
    }

    private fun startCalendar() {
        val today = LocalDate.now() // 현재 시간을 가지는 LocalDate 인스턴스 생성
        val year = today.year
        val month = today.monthValue
        val day = today.dayOfMonth
        binding.calendar.setSelectedDate(CalendarDay.today())
        val Date =
            "${year}${month.toString().padStart(2, '0')}${day.toString().padStart(2, '0')}"
        mainViewModel.selectedDate = Date

    }

    private fun observeViewModel() {
        mainViewModel.getUserInfoEvent.observe(this) { team ->
            schedule()
        }
    }

    private fun getdata() {
        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month
            val day = date.day
            val Date =
                "${year}${month.toString().padStart(2, '0')}${day.toString().padStart(2, '0')}"
//            shortShowToast(Date)
            mainViewModel.selectedDate = Date
            mainViewModel.getRoutine(Date)
            mainViewModel.getHandover(Date, mainViewModel.selectedPosition)
        }
    }

    private fun schedule() {
        val processList = Organization.team.toTypedArray()
        val team = mainViewModel.team

        val index = processList.indexOf(team)
        val decoratorM = DecoratorM(requireContext(), index)
        binding.calendar.addDecorator(decoratorM)
        val decoratorN = DecoratorN(requireContext(), index)
        binding.calendar.addDecorator(decoratorN)
//
        val decoratorE = DecoratorE(requireContext(), index)
        binding.calendar.addDecorator(decoratorE)
        val decoratorOff = DecoratorOff(requireContext(), index)
        binding.calendar.addDecorator(decoratorOff)
//        binding.calendar.tileHeight = -2
        binding.calendar.tileWidth = -3
        binding.calendar.setDateTextAppearance(R.style.CustomDateTextAppearance)

    }
}