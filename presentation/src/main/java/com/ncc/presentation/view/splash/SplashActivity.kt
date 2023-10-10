package com.ncc.presentation.view.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseActivity
import com.ncc.presentation.databinding.ActivitySplashBinding
import com.ncc.presentation.view.main.MainActivity
import com.ncc.presentation.view.main.auth.LoginFragment
import com.ncc.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val splashViewModel by viewModels<SplashViewModel>()
    private val appVersion = "1.0.0"

    override fun init() {
        splashViewModel.checkAppVersion()
            .addOnSuccessListener {
                if (appVersion == it.value) {
//                    longShowToast("앱 버전: ${it.value}")
                    this.startActivityAndFinish(this, MainActivity::class.java)
                } else longShowToast("앱 버전이 다릅니다! ${it.value}")
            }
            .addOnFailureListener {
                shortShowToast("오류가 발생했습니다, 오류코드 - ${it.message}")
            }
    }
}

//intent and finish
fun AppCompatActivity.startActivityAndFinish(context: Context, activity: Class<*>) {
    startActivity(Intent(context, activity))
//    startActivity(Intent(context, activity).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    this.finish()
}