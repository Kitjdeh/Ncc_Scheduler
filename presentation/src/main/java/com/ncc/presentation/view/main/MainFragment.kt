package com.ncc.presentation.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentMainBinding
import com.ncc.presentation.databinding.FragmentRoutineBinding
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.viewmodel.SplashViewModel

class MainFragment :
    BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val splashViewModel by activityViewModels<SplashViewModel>()
    override fun init() {
        binding.fragment = this
        loadUserInfo()
        observeViewModel()
    }


    private fun loadUserInfo() {
        splashViewModel.getUserId()
        val useruid = splashViewModel.useruid.value.toString()
        Log.d("로컬 uid 호출", useruid)
        mainViewModel.getUserInfo(useruid)
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        mainViewModel.getUserInfoEvent.observe(this) { user ->
////            val userName = user.name
//            Log.d("유저 정보 UI 적용", mainViewModel.selectedPosition)
//
//            binding.userInfo.text = "${mainViewModel.user}-${mainViewModel.team}"
            Log.d("유저 정보 UI 적용", user.toString())

            binding.userInfo.text = "${user.name} ${user.team}조"
            Log.d("유저 정보 UI 적용", user.toString())
            binding.userTeam.text = "${user.position}"
        }
//        mainViewModel.getHandovereEvent.observe(this) {
//            Log.d("getHandovereEvent", this.toString())
//        }
    }

}