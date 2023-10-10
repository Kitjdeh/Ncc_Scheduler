package com.ncc.presentation.view.main


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseActivity
import com.ncc.presentation.databinding.ActivityMainBinding
import com.ncc.presentation.view.main.handover.HandoverFragment
import com.ncc.presentation.view.main.routine.RoutineFragment
import com.ncc.presentation.view.main.setting.SettingActivity
import com.ncc.presentation.view.splash.SplashActivity
import com.ncc.presentation.view.splash.startActivityAndFinish
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mainViewModel by viewModels<MainViewModel>()
    private val splashViewModel by viewModels<SplashViewModel>()

    //    private val mainViewModel by activityViewModels<MainViewModel>()
    private val routineFragment = RoutineFragment()
    private val handoverFragment = HandoverFragment()

    override fun init() {
        mainViewModel.getRoutine()
        binding.main = this
        initNavition()
        Log.d("메인 시작,", mainViewModel.userUid)
        binding.settingBtn.setOnClickListener {
            this.startActivityAndFinish(this, SettingActivity::class.java)
        }

    }

//    private fun loadUserInfo() {
//        splashViewModel.getUserId()
//        val useruid = splashViewModel.useruid.value.toString()
//        Log.d("로컬 uid 호출", useruid)
//        mainViewModel.getUserInfo(useruid)
//        binding.userInfo.text = "${mainViewModel.user}-${mainViewModel.team}"
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun observeViewModel() {
//        mainViewModel.getUserInfoEvent.observe(this) {
////            val userName = user.name
//            Log.d("유저 정보 UI 적용", mainViewModel.selectedPosition)
//
//            binding.userInfo.text = "${mainViewModel.user}-${mainViewModel.team}"
////            Log.d("유저 정보 UI 적용", userName)
////
////            binding.userInfo.text = "${user.name}-${user.team}"
////            Log.d("유저 정보 UI 적용", user.toString())
//        }
//        mainViewModel.getHandovereEvent.observe(this) {
//            Log.d("getHandovereEvent", this.toString())
//        }
//    }

    private fun initNavition() {
        binding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.routine -> {
                        fragmentnavigation(routineFragment)
                    }

                    R.id.handover -> {
                        fragmentnavigation(handoverFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.routine
        }
    }

    private fun fragmentnavigation(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}