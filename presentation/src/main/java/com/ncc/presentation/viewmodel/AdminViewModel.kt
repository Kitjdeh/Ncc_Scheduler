package com.ncc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.usecase.routine.GetRoutineUsecase
import com.ncc.domain.usecase.routine.SetRoutineUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getRoutineUsecase: GetRoutineUsecase,
    private val setRoutineUsecase: SetRoutineUsecase,
) : ViewModel() {

    private var _getRoutineEvent = MutableLiveData<List<DomainRoutine>>()
    val getRoutineEvent: LiveData<List<DomainRoutine>> get() = _getRoutineEvent

    private var _month = MutableLiveData<List<String>>()

    private var _week = MutableLiveData<List<String>>()
    val weekEvent: LiveData<List<String>> get() = _week


    private var _dayOfWeek = MutableLiveData<List<String>>()

    private var _type = MutableLiveData<String>()
    val changeTypeEvent: LiveData<String> get() = _type


    private var _selectedMonth = MutableLiveData<BooleanArray>()
    val selectedMonthEvent: LiveData<BooleanArray> get() = _selectedMonth
    val monthEvent: LiveData<List<String>> get() = _month


    private var _selectedDayOfMonth = MutableLiveData<BooleanArray>()
    val selectedDayOfMonthEvent: LiveData<BooleanArray> get() = _selectedDayOfMonth

    val dayOfMonthEvent: LiveData<List<String>> get() = _dayOfMonth
    private var _dayOfMonth = MutableLiveData<List<String>>()


    private var _selectedPosition = MutableLiveData<BooleanArray>()
    val selectedPosition: LiveData<BooleanArray> get() = _selectedPosition
    private var _position = MutableLiveData<List<String>>()


    private var _selectedDayOfWeek = MutableLiveData<BooleanArray>()
    val selectedDayOfWeekEvent: LiveData<BooleanArray> get() = _selectedDayOfWeek
    val dayOfWeekEvent: LiveData<List<String>> get() = _dayOfWeek


    private var _selectedWeek = MutableLiveData<BooleanArray>()
    val selectedWeekEvent: LiveData<BooleanArray> get() = _selectedWeek

    private var _selectedDay = MutableLiveData<BooleanArray>()
    val selectedDayEvent: LiveData<BooleanArray> get() = _selectedDay


    var userName = "장현수"
    var title: String = ""
    var team: String = ""
    var date: String = startCalendar()
    var type: String = ""
    var month: List<String>? = null
    var week: List<String>? = null
    var dayOfMonth: List<String>? = null
    var dayOfWeek: List<String>? = null

    var selectedShift = "전체 보기"
    var selectedType = "전체 보기"
    var listRoutine: List<DomainRoutine>? = null
    val monthCheckedItems = BooleanArray(13)
    fun clear() {
    }

    fun getRoutineList() {
        viewModelScope.launch {
            val data = getRoutineUsecase.getRoutine()
            _getRoutineEvent.setValue(data)
        }
    }

    suspend fun deleteRoutine(uid: String): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            getRoutineList()
            val result = getRoutineUsecase.deleteRoutine(uid)
            getRoutineUsecase.resetRoutine()
            getRoutineList()
            result
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

    fun selectPositionEvent(data: BooleanArray) {
        _selectedPosition.postValue(data)
    }

    fun selectPosition(data: List<String>) {
        _position.postValue(data)
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

    fun filteringTypeRoutineList(type: String) {
        viewModelScope.launch {
//            var routineList = getRoutineUsecase.getRoutine()
////            var routineList = _getRoutineEvent.value
//            suspendCoroutine<List<DomainRoutine>> { continuation ->
//                if (data != "전체 보기") {
//                    routineList = routineList!!.filter { routine -> routine.type == data }
//                }
//                continuation.resume(routineList!!)
//                _getRoutineEvent.postValue(routineList!!)
            selectedType = type
            filteringRoutine(selectedShift, selectedType)
        }

    }

    fun filteringShiftRoutineList(shift: String) {
        viewModelScope.launch {
            selectedShift = shift
            filteringRoutine(selectedShift, selectedType)
        }
    }
//    fun filteringShiftRoutineList(data: String) {
//        viewModelScope.launch {
////            var routineList = _getRoutineEvent.value
//            var routineList = getRoutineUsecase.getRoutine()
//            Log.d("데이터1${data}", routineList.toString())
//            suspendCoroutine<List<DomainRoutine>> { continuation ->
//                if (data != "전체 보기") {
//                    Log.d("데이터4${data}", routineList.toString())
//                    routineList = routineList!!.filter { routine -> routine.team == data }
//                }
//                continuation.resume(routineList!!)
//                Log.d("데이터2${data}", routineList.toString())
//                _getRoutineEvent.postValue(routineList!!)
//            }
//            Log.d("데이터3${data}", routineList.toString())
//        }
//    }

    private fun filteringRoutine(shift: String, work: String) {
        selectedShift = shift
        selectedType = work
        viewModelScope.launch {
            var routineList = getRoutineUsecase.getRoutine()
            suspendCoroutine<List<DomainRoutine>> { continuation ->
                if (shift != "전체 보기" && work != "전체 보기") {
                    routineList =
                        routineList.filter { routine -> routine.type == work && routine.team == shift }
//                    Log.d("둘다 전체보기", routineList.toString())
                } else if (shift == "전체 보기" && work != "전체 보기") {
                    routineList =
                        routineList.filter { routine -> routine.type == work }
//                    Log.d("shift만 전체보기", routineList.toString())
                } else if (shift != "전체 보기" && work == "전체 보기") {
                    routineList =
                        routineList.filter { routine -> routine.team == shift }
//                    Log.d("work만 전체보기", routineList.toString())
                } else {
//                    Log.d("둘다 전체보기", routineList.toString())
                }
                continuation.resume(routineList)
                _getRoutineEvent.postValue(routineList!!)
            }
        }
    }


//    suspendCoroutine<List<DataRoutine>> { continuation ->
//        if (routineList != null) {
//            Log.d("파베 요청 없음", routineList.toString())
//            continuation.resume(routineList!!)
//        } else {
//            val routineCollectionRef =
//                firestore.collection("routine")
//                    .orderBy("team", Query.Direction.ASCENDING).get()
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            routineList = it.result.toObjects(DataRoutine::class.java)
//                            continuation.resume(routineList!!)
//                        } else {
//                        }
//                    }
//        }
//    }

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
            position = _position.value!!,
            content = content,
            date = date,
            type = type,
            month = month,
            week = week,
            dayOfMonth = dayOfMonth,
            dayOfWeek = dayOfWeek
        )
        Log.d("set루틴 작동", data.toString())
        setRoutineUsecase.execute(data)
        team = ""
        date = startCalendar()
        type = ""
        month = null
        week = null
        dayOfMonth = null
        dayOfWeek = null
    }
}