package com.ncc.presentation.view.main.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannedString
import android.util.Log
import com.ncc.presentation.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


class DecoratorE(context: Context, idx: Int) : DayViewDecorator {
    private val idx = idx
    @SuppressLint("UseCompatLoadingForDrawables")
    val drawable = context.getDrawable(R.drawable.e)
    override fun shouldDecorate(day: CalendarDay?): Boolean {

        val startDate = CalendarDay.from(2023, 9, 2)
        if (day != null) {
            val cnt = (day.day - startDate.day) % 19
            return when (idx) {
                0 -> {
                    cnt in 12..14
                }
                1 -> {
                    cnt in 8..11
                }
                2 -> {
                    cnt in 4..7
                }
                else -> {
                    cnt in 0..3
                }
            }

        }
        return false
//        return day != null && day.isAfter(today)
    }

    override fun decorate(view: DayViewFacade?) {
        // 날짜에 따라 텍스트를 추가하거나 스타일을 변경할 수 있습니다.
        if (view != null) {

            if (drawable != null) {
                view.setBackgroundDrawable(drawable)
            }

        }
    }
}