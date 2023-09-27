package com.ncc.presentation.view.main.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannedString
import android.util.Log
import com.ncc.presentation.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class DecoratorN(context: Context, idx: Int) : DayViewDecorator {
    private val idx = idx
    @SuppressLint("UseCompatLoadingForDrawables")
    val drawable = context.getDrawable(R.drawable.n)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        // 오늘 날짜 이후인 경우에만 장식을 적용할 것입니다.
        val startDate = CalendarDay.from(2023, 9, 2)
        if (day != null) {
            val cnt = (day.day - startDate.day) % 19
            return when (idx) {
                0 -> {
                    cnt in 4..7
                }
                1 -> {
                    cnt in 0..3
                }
                2 -> {
                    cnt in 15..18
                }
                else -> {
                    cnt in 11..14
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