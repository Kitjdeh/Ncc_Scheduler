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
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar


class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun init() {
        getdata()
        startCalendar()
        observeViewModel()
        schedule()
        binding.calendar.selectionColor
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


    private fun CalendarDay.toLocalDate(): LocalDate {
        val zoneId = ZoneId.systemDefault() // 현재 시스템의 타임존 사용
        return LocalDate.of(year, month, day).atStartOfDay(zoneId).toLocalDate()
    }

    private fun observeViewModel() {
        mainViewModel.checkUserInfoEvent.observe(this) { user ->
            Log.d("달력observeViewmodel", user.toString())
            schedule()
        }
        binding.calendar.setDateTextAppearance(15)
    }

    private fun getdata() {
        val dayOfWeekList = Organization.dayOfWeekList.toTypedArray()

        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            // 현재 날짜를 가져옵니다.

            checkshift(date)
            val calendar = Calendar.getInstance()
            val Ayear = date.year
            val Amonth = date.month - 1
            val Aday = date.day
            calendar.set(Ayear, Amonth, Aday)
            val WEEK_OF_MONTH = calendar.get(Calendar.WEEK_OF_MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)
            val dayOfWeek = dayOfWeekList[dayOfWeekIndex]
//            Log.d("${date}", WEEK_OF_MONTH.toString())
//            Log.d("주차확인", " dayOfWeekIndex ${dayOfWeekIndex}  dayOfWeek ${dayOfWeek}.")
            val year = date.year
            val month = date.month
            val day = date.day
            val Date =
                "${year}${month.toString().padStart(2, '0')}${day.toString().padStart(2, '0')}"
            mainViewModel.changeDate(Date)
            mainViewModel.filterRoutine(Date, dayOfWeek, WEEK_OF_MONTH.toString())
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
        val decoratorE = DecoratorE(requireContext(), index)
        binding.calendar.addDecorator(decoratorE)
        val decoratorOff = DecoratorOff(requireContext(), index)
        binding.calendar.addDecorator(decoratorOff)
        binding.calendar.tileWidth = -3
        binding.calendar.setDateTextAppearance(R.style.CustomDateTextAppearance)

    }

    private fun checkshift(date: CalendarDay) {
        //    val startDate = LocalDate.of(2023, 9, 2)
        val startDate = Organization.startDate
        val localDate = date.toLocalDate()
        val cnt = ChronoUnit.DAYS.between(startDate, localDate).toInt() % 19
        val team = mainViewModel.team
        val index = Organization.team.toTypedArray().indexOf(team)
        var shift = ""
        when (index) {
            0 -> {
                shift = when {
                    cnt in 0..3 -> "MOR"
                    cnt in 4..7 -> "NIG"
                    cnt in 12..14 -> "EVE"
                    cnt in 8..11 || cnt in 16..18 -> "OFF"
                    else -> ""
                }
            }

            1 -> {
                shift = when {
                    cnt in 15..18 -> "MOR"
                    cnt in 0..3 -> "NIG"
                    cnt in 8..11 -> "EVE"
                    (cnt in 4..7) || (cnt in 12..14) -> "OFF"
                    else -> ""
                }
            }

            2 -> {
                shift = when {
                    cnt in 11..14 -> "MOR"
                    cnt in 15..18 -> "NIG"
                    cnt in 4..7 -> "EVE"
                    (cnt in 0..3) || (cnt in 8..10) -> "OFF"
                    else -> ""
                }
            }

            else -> {
                shift = when {
                    cnt in 7..11 -> "MOR"
                    cnt in 11..14 -> "NIG"
                    cnt in 0..3 -> "EVE"
                    cnt in 4..7 || (cnt in 15..18) -> "OFF"
                    else -> ""
                }
            }
        }
        mainViewModel.changeShift(shift)
    }
}