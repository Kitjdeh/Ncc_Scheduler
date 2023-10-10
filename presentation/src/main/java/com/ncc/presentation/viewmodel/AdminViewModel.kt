package com.ncc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.usecase.routine.GetRoutineUsecase
import com.ncc.domain.usecase.routine.SetRoutineUsecase
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getRoutineUsecase: GetRoutineUsecase,
    private val setRoutineUsecase: SetRoutineUsecase,
) : ViewModel() {

    private var _getRoutineEvent = MutableLiveData<List<DomainRoutine>>()
    val getRoutineEvent: LiveData<List<DomainRoutine>> get() = _getRoutineEvent

    private var _month = MutableLiveData<List<String>>()

    private var _week = MutableLiveData<List<String>>()
    private var _dayOfMonth = MutableLiveData<List<String>>()
    private var _dayOfWeek = MutableLiveData<List<String>>()

    private var _type = MutableLiveData<String>()
    val changeTypeEvent: LiveData<String> get() = _type


    private var _selectedMonth = MutableLiveData<BooleanArray>()
    val selectedMonthEvent: LiveData<BooleanArray> get() = _selectedMonth

    private var _selectedDayOfMonth = MutableLiveData<BooleanArray>()
    val selectedDayOfMonthEvent: LiveData<BooleanArray> get() = _selectedDayOfMonth

    private var _selectedDayOfWeek = MutableLiveData<BooleanArray>()
    val selectedDayOfWeekEvent: LiveData<BooleanArray> get() = _selectedDayOfWeek

    private var _selectedWeek = MutableLiveData<BooleanArray>()
    val selectedWeekEvent: LiveData<BooleanArray> get() = _selectedWeek

    private var _selectedDay = MutableLiveData<BooleanArray>()
    val selectedDayEvent: LiveData<BooleanArray> get() = _selectedDay


    val monthEvent: LiveData<List<String>> get() = _month
    val weekEvent: LiveData<List<String>> get() = _week
    val dayOfMonthEvent: LiveData<List<String>> get() = _dayOfMonth
    val dayOfWeekEvent: LiveData<List<String>> get() = _dayOfWeek

    var userName = "장현수"
    var title: String = ""
    var team: String = ""
    var date: String = startCalendar()
    var type: String = ""
    var month: List<String>? = null
    var week: List<String>? = null
    var dayOfMonth: List<String>? = null
    var dayOfWeek: List<String>? = null

    val monthCheckedItems = BooleanArray(13)
    fun clear() {
    }

    fun getRoutineList() {
        viewModelScope.launch {
            val data = getRoutineUsecase.getRoutine()
            _getRoutineEvent.setValue(data)
        }
    }

    private fun startCalendar(): String {
        val today = LocalDate.now() // 현재 시간을 가지는 LocalDate 인스턴스 생성
        val year = today.year
        val month = today.monthValue
        val day = today.dayOfMonth
        val Date =
            "${year}${month.toString().padStart(2, '0')}${day.toString().padStart(2, '0')}"
        return Date
    }

    fun changeShift(data: String) {
        team = data
    }

    fun changeWork(data: String) {
        type = data
        _type.postValue(data)
    }

    fun checkWeekSelected(data: BooleanArray) {
        _selectedWeek.postValue(data)
    }

    fun selectWeek(data: List<String>) {
        _week.postValue(data)
        week = data
    }

    fun checkMonthSelected(data: BooleanArray) {
        _selectedMonth.postValue(data)
    }

    fun checkDayOfMonthSelected(data: BooleanArray) {
        _selectedDayOfMonth.postValue(data)
    }

    fun checkDayOfWeekSelected(data: BooleanArray) {
        _selectedDayOfWeek.postValue(data)
    }

    fun selectMonth(data: List<String>) {
        _month.postValue(data)
        month = data
    }

    fun selectDayOfWeek(data: List<String>) {
        _dayOfWeek.postValue(data)
        dayOfWeek = data
    }

    fun selectDayOfMonth(data: List<String>) {
        _dayOfMonth.postValue(data)
        dayOfMonth = data
    }

    //    fun setRoutine(data: DomainRoutine) {
    fun setRoutine(title: String, content: String) {
        val data = DomainRoutine(
            name = userName,
            id = "",
            title = title,
            team = team,
            date = date,
            type = type,
            month = month,
            week = week,
            dayOfMonth = dayOfMonth,
            dayOfWeek = dayOfWeek
        )
        Log.d("set루틴 작동", data.toString())
        setRoutineUsecase.execute(data)
    }
}