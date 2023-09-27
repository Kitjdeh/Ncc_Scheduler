package com.ncc.presentation.view.main.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ncc.domain.utils.ErrorType
import com.ncc.domain.utils.ScreenState
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseActivity
import com.ncc.presentation.databinding.ActivityLoginBinding
import com.ncc.presentation.databinding.ActivityMainBinding
import com.ncc.presentation.view.splash.SplashActivity
import com.ncc.presentation.view.splash.startActivityAndFinish
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val mainViewModel by viewModels<MainViewModel>()
    private val splashViewModel by viewModels<SplashViewModel>()
    private var id = ""
    private var password = ""
    private var useruid = ""

    //    val navController = findNavController(R.id.activity_login)
    override fun init() {
        binding.activity = this
        observeViewModel()
        splashViewModel.getIsLogin()
        splashViewModel.getUserId()
    }

    fun loginBtn(view: View) {
        id = binding.id.text.toString()

        password = binding.password.text.toString()
        splashViewModel.login(id, password).addOnSuccessListener { authResult ->
            val uid = authResult.user?.uid
            if (uid != null) {

                splashViewModel.saveId(id, password, uid)
            } else {
                shortShowToast("로그인 정보 자동저장 실패")
            }
        }
    }
    private fun observeViewModel() {
        splashViewModel.loginCallEvent.observe(this) {
            binding.loadingBar.visibility = View.INVISIBLE
            when (it) {
                ScreenState.LOADING -> {

                    mainViewModel.userUid = useruid

                    this.startActivityAndFinish(this, SplashActivity::class.java)
                }
//                    this.findNavController().navigate(R.id.action_loginActivity_to_splashActivity)
                ScreenState.ERROR -> shortShowToast("로그인 실패")
                else -> shortShowToast("원인을 알 수 없는 오류가 발생했습니다.")
            }
        }
        splashViewModel.isLogin.observe(this) {
            binding.loadingBar.visibility = View.INVISIBLE
            when (it) {
                true -> {
                    var userid = splashViewModel.userid.value.toString()
                    var userpwd = splashViewModel.userpwd.value.toString()
                    useruid = splashViewModel.useruid.value.toString()
                    binding.password.text = Editable.Factory.getInstance().newEditable(userpwd)
                    binding.id.text = Editable.Factory.getInstance().newEditable(userid)
                    mainViewModel.userUid = userid

                    splashViewModel.login(userid, userpwd)
                }

                false -> {}
            }
        }
    }
}