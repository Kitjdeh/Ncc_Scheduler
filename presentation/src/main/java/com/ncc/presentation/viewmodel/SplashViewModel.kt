package com.ncc.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncc.domain.usecase.CheckAppVersionUseCase
import com.ncc.domain.usecase.GetIdUsecase
import com.ncc.domain.usecase.GetIsLogin
import com.ncc.domain.usecase.LoginUsecase
import com.ncc.domain.usecase.LogoutUserCase
import com.ncc.domain.usecase.SaveIdUsecase
import com.ncc.domain.utils.RemoteErrorEmitter
import com.ncc.domain.utils.ScreenState
import com.ncc.presentation.widget.utils.SingleLiveEvent

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAppVersionUsecase: CheckAppVersionUseCase,
    private val loginUsecase: LoginUsecase,
    private val getIsLogin: GetIsLogin,
    private val saveIdUsecase: SaveIdUsecase,
    private val getIdUsecase: GetIdUsecase,
    private val logoutUserCase: LogoutUserCase
) : ViewModel() {
    var userid = MutableLiveData<String>()
    var userpwd = MutableLiveData<String>()
    var useruid = MutableLiveData<String>()
    val isLogin: LiveData<Boolean> get() = _isLogin
    private var _isLogin = SingleLiveEvent<Boolean>()


    val loginCallEvent: LiveData<ScreenState> get() = _loginCallEvent
    private var _loginCallEvent = SingleLiveEvent<ScreenState>()

    fun checkAppVersion() = checkAppVersionUsecase.execute()


    fun login(id: String, pwd: String) =
        loginUsecase.execute(
            id, pwd
        ).let { response ->
            response.addOnSuccessListener {
                _loginCallEvent.postValue(ScreenState.LOADING)
            }
                .addOnFailureListener {
                    _loginCallEvent.postValue(ScreenState.ERROR)
                }
        }

    fun saveId(id: String, pwd: String, uid: String) =
        viewModelScope.launch {
            Log.d("자동저장", "${uid}")
            saveIdUsecase.execute(id, pwd, uid)
        }

    fun getUserId() =
        viewModelScope.launch {
            getIdUsecase.execute().collect { LoginInfo ->
                if (LoginInfo.isNotEmpty()) {
                    Log.d("자동저장정보", LoginInfo.toString())
                    userid.value = LoginInfo.first()
                    userpwd.value = LoginInfo[1]
                    useruid.value = LoginInfo.last()
                }
            }
        }

    fun getIsLogin() =
        viewModelScope.launch {
            getIsLogin.execute().collect() { result ->
                _isLogin.postValue(result)
            }
        }

    fun logout() {
        viewModelScope.launch {
            logoutUserCase.execute().collect() { result ->
                if (result) {
                    _isLogin.postValue(false) // 자동 로그인을 비활성화
                } else {
                }
            }
        }
    }
}