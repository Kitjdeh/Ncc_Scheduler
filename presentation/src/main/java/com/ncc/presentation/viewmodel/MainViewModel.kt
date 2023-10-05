package com.ncc.presentation.viewmodel


import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.ncc.domain.model.DomainComment
import com.ncc.domain.model.DomainHandover
import com.ncc.domain.model.DomainRoutine
import com.ncc.domain.model.DomainUser
import com.ncc.domain.usecase.GetIdUsecase
import com.ncc.domain.usecase.GetUserInfoUsecase
import com.ncc.domain.usecase.UpdateUserInfoUsecase
import com.ncc.domain.usecase.handover.GetHandoverUsecase
import com.ncc.domain.usecase.routine.GetRoutineUsecase
import com.ncc.domain.usecase.handover.SetCommentUsecase
import com.ncc.domain.usecase.handover.SetHandoverUsecase
import com.ncc.domain.usecase.routine.SetRoutineUsecase
import com.ncc.domain.utils.ErrorType
import com.ncc.domain.utils.RemoteErrorEmitter
import com.ncc.domain.utils.ScreenState
import com.ncc.presentation.widget.utils.Organization
import com.ncc.presentation.widget.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHandoverUseCase: GetHandoverUsecase,
    private val setHandoverUsecase: SetHandoverUsecase,
    private val getRoutineUsecase: GetRoutineUsecase,
    private val setRoutineUsecase: SetRoutineUsecase,
    private val setCommentUsecase: SetCommentUsecase,
    private val getIdUsecase: GetIdUsecase,
    private val getUserInfoUsecase: GetUserInfoUsecase,
    private val updateUserInfoUsecase: UpdateUserInfoUsecase
//    private val loginUsecase: LoginUsecase,
//    private val setCommentUsecase: SetCommentUsecase


) : ViewModel(), RemoteErrorEmitter {

    private var _getRoutineEvent = MutableLiveData<List<DomainRoutine>>()
    val getRoutineEvent: LiveData<List<DomainRoutine>> get() = _getRoutineEvent


//    val getRoutineEvent: LiveData<List<DomainRoutine>> get() = _getRoutineEvent
//    private var _getRoutineEvent = SingleLiveEvent<List<DomainRoutine>>()

    val getHandovereEvent: LiveData<List<DomainHandover>> get() = _getHandovereEvent
    private var _getHandovereEvent = SingleLiveEvent<List<DomainHandover>>()

    val selectedDateEvent: LiveData<String> get() = _selectedDateEvent
    private var _selectedDateEvent = SingleLiveEvent<String>()

    val getUserInfoEvent: LiveData<DomainUser> get() = _getUserInfoEvent
    private var _getUserInfoEvent = SingleLiveEvent<DomainUser>()

    val getTeamChangeEvent: LiveData<String> get() = _getTeamChangeEvent
    private var _getTeamChangeEvent = SingleLiveEvent<String>()

    val getPositionChangeEvent: LiveData<String> get() = _getPositionChangeEvent
    private var _getPositionChangeEvent = SingleLiveEvent<String>()

    val updateCallEvent: LiveData<ScreenState> get() = _updateCallEvent
    private var _updateCallEvent = SingleLiveEvent<ScreenState>()

    var userUid = ""

    lateinit var registerForCommentActivityResult: ActivityResultLauncher<Intent>


    val routineList = arrayListOf<DomainRoutine>()


    val handoverList = arrayListOf<DomainHandover>()
    val commentList = arrayListOf<DomainComment>()

    val handoverImageList = arrayListOf<String>()
    val imageList = arrayListOf<String>()
    val commentImageList = arrayListOf<String>()
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("YYYY년 MM월 d일", Locale.getDefault())
    var selectedDate = formmatdate()
    var selectedPosition = ""
    var user = ""
    var team = ""
    var position = ""
    var id = 0

    fun updateUserInfo() {
        val data = DomainUser(id, user, team, position, userUid)
        Log.d("유저정보 갱신 데이터", data.toString())
        updateUserInfoUsecase.execute(data).addOnSuccessListener {
            _updateCallEvent.postValue(ScreenState.LOADING)
        }.addOnFailureListener { _updateCallEvent.postValue(ScreenState.ERROR) }
    }

    fun viewPosition(position: String) {
        selectedPosition = position
        getHandover(selectedDate, selectedPosition)
    }

    fun changePosition(nowposition: String) {
        _getPositionChangeEvent.value = nowposition
        position = nowposition
        selectedPosition = nowposition
    }

    fun changeTeam(nowteam: String) {
        _getTeamChangeEvent.value = nowteam
        team = nowteam
    }


    fun getUserInfo(uid: String) {
        getUserInfoUsecase.execute(uid).addOnSuccessListener { DocumentSnapshot ->
            if (DocumentSnapshot.exists()) {
                val data = DocumentSnapshot.toObject(DomainUser::class.java)

                team = data!!.team
                user = data!!.name
                position = data!!.position
                selectedPosition = data!!.position
                id = data!!.id
                userUid = data!!.uid
//                _getUserInfoEvent.value = data!!
                Log.d("겟유저인포 작동", _getUserInfoEvent.value.toString())
//                _getUserInfoEvent.call()
                _getUserInfoEvent.postValue(data!!)
            }
        }
    }


    fun getHandoverImage(data: DomainHandover) {
        imageList.clear()
        if (data.ImageSrc != null) {
            for (src in data.ImageSrc!!) {
                imageList.add(src)
            }
        }
    }

    fun getComment(data: DomainHandover) {
        commentList.clear()
        if (data.comment != null) {
            for (comment in data.comment!!) {
                commentList.add(comment)
            }
        }

    }

    fun getHandover(date: String, selectedPosition: String) {
        handoverList.clear()
        val positionList = Organization.position.toTypedArray()
        if (selectedPosition == "전체보기") {
            viewModelScope.launch(Dispatchers.IO) {
                for (position in positionList) {
                    try {
                        val snapshot = getHandoverUseCase.execute(date, position).await()

                        for (item in snapshot.documents) {
                            item.toObject(DomainHandover::class.java)?.let {
                                handoverList.add(it)
                            }
                        }
                        Log.d("전체 요청 생성3", "${handoverList.toString()} ${team}")
                    } catch (e: Exception) {
                        // 에러 처리
                    }
                }
                withContext(Dispatchers.Main) {
                    // LiveData 값을 업데이트
                    _getHandovereEvent.postValue(handoverList)
                    Log.d("핸드오버 요정_getHandovereEvent", getHandovereEvent.value.toString())
                }
            }

        } else {

            getHandoverUseCase.execute(date, selectedPosition)
                .addOnSuccessListener { snapshot ->
                    handoverList.clear()
                    for (item in snapshot.documents) {
                        item.toObject(DomainHandover::class.java).let {
                            handoverList.add(it!!)
                        }
                    }
//                    Log.d("전체 요청 생성3", handoverList.toString())
                    _getHandovereEvent.call()
                }


        }


    }

    fun formmatdate(): String {
        val year = currentDate.year
        val month = currentDate.month
        val day = currentDate.dayOfMonth
        val Date =
            "${year}${month.toString().padStart(2, '0')}${day.toString().padStart(2, '0')}"
        return Date
    }

    fun getRoutine(date: String) {
        viewModelScope.launch() {
            val result = getRoutineUsecase.execute(date)
            Log.d("뷰모델 데이터 적용", Thread.currentThread().name)
            _getRoutineEvent.setValue(result)
        }
    }
//    fun getRoutine(date: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = getRoutineUsecase.execute(date)
//            withContext(Dispatchers.Main) {
//                Log.d("데이터 적용", Thread.currentThread().name)
//                _getRoutineEvent.setValue(result)
//            }
//        }
//    }
//    fun getRoutine(date: String) {
//
//        routineList.clear()
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = getRoutineUsecase.execute(date)
//            Log.d("쓰레드 ", Thread.currentThread().name)
//            withContext(Dispatchers.Main) {
//                _getRoutineEvent.postValue(result)
//                routineList.addAll(result)
//            }
//        }
//        Log.d("데이터 적용", Thread.currentThread().name)
//    }
//    fun getRoutine(date: String) {
//        routineList.clear()
//        getRoutineUsecase.execute(date)
//            .addOnSuccessListener { snapshot ->
//
//                for (item in snapshot.documents) {
//                    item.toObject(DomainRoutine::class.java).let {
//                        it!!.filtering()
//                        routineList.add(it!!)
//                    }
//                }
//                _getRoutineEvent.call()
//            }
//    }


    fun setHandover(
        name: String,
        id: String,
        routine: String,
        title: String,
        date: String,
        time: String,
        content: String,
        imageUri: List<String>?,
        position: String
    ) {
        Log.d("인수인계 시작", "${position},${id}")
        setHandoverUsecase.execute(
            DomainHandover(
                name, id, routine, title, date, time, content, imageUri, comment = null, position
            ), position
        )
    }

    fun setComment(
        name: String,
        handoverid: String,
        id: String,
        content: String,
        ImageSrc: List<String>?,
        date: String,
        time: String,
        position: String
    ) {
        setCommentUsecase.execute(
            DomainComment(name, handoverid, id, content, ImageSrc, date, time), position
        ).addOnSuccessListener { snapshot ->
            Log.d(
                "NCC댓글 viewmodel",
                "${
                    DomainComment(
                        name,
                        handoverid,
                        id,
                        content,
                        ImageSrc,
                        date,
                        time
                    ).toString()
                }${position}"
            )
            commentImageList.clear()
        }

    }

    fun imageClear() {
        imageList.clear()
    }

    fun commentImageClear() {
        commentImageList.clear()
    }

    fun formatDate(inputDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("YYYY년 MM월 dd일")
        val date = LocalDate.parse(inputDate, DateTimeFormatter.BASIC_ISO_DATE)
        return date.format(formatter)
    }


    override fun onError(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onError(errorType: ErrorType) {
        TODO("Not yet implemented")

    }

}