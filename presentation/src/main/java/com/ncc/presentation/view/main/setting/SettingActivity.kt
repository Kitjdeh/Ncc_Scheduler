package com.ncc.presentation.view.main.setting

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ncc.domain.utils.ScreenState
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseActivity
import com.ncc.presentation.databinding.ActivityMainBinding
import com.ncc.presentation.databinding.ActivitySettingBinding
import com.ncc.presentation.view.admin.AdminActivity
import com.ncc.presentation.view.main.MainActivity
import com.ncc.presentation.view.main.auth.LoginActivity
import com.ncc.presentation.view.splash.SplashActivity
import com.ncc.presentation.view.splash.startActivityAndFinish
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.viewmodel.SplashViewModel
import com.ncc.presentation.widget.utils.Organization
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    private val mainViewModel by viewModels<MainViewModel>()
    private val splashViewModel by viewModels<SplashViewModel>()

    override fun init() {
        loadUserInfo()
        binding.setting = this
        observeViewModel()
        binding.goMainBtn.setOnClickListener {
            mainViewModel.updateUserInfo()

        }
        binding.selectTeamBtn.setOnClickListener {
            changeTeam()
        }
        binding.selectPositionBtn.setOnClickListener {
            changePosition()
        }
        binding.logoutBtn.setOnClickListener {
            splashViewModel.logout()
            shortShowToast("로그아웃 되었습니다.")
            this.startActivityAndFinish(this, LoginActivity::class.java)
        }
        binding.goAdminBtn.setOnClickListener {
            this.startActivityAndFinish(this, AdminActivity::class.java)

        }
    }


    private fun loadUserInfo() {
        splashViewModel.getUserId()
        val useruid = splashViewModel.useruid.value.toString()
        mainViewModel.getUserInfo(useruid)
    }

    private fun observeViewModel() {
        mainViewModel.getTeamChangeEvent.observe(this) { team ->
            binding.selectTeamBtn.text = team
        }
        mainViewModel.getPositionChangeEvent.observe(this) { position ->
            binding.selectPositionBtn.text = position

        }
        mainViewModel.getUserInfoEvent.observe(this) { user ->
            binding.selectTeamBtn.text = user.team
            binding.selectPositionBtn.text = user.position
            Log.d("유저UID", mainViewModel.userUid)
            if (mainViewModel.userUid == "EHGAPw6YdsdiV57wScCfkBkby6z1" || mainViewModel.userUid == "NXWWGuqiaPQS09iMvtyA8azxcJE2") {
                binding.goAdminBtn.visibility = View.VISIBLE
            } else {
                binding.goAdminBtn.visibility = View.INVISIBLE
            }
        }

        mainViewModel.updateCallEvent.observe(this) {
            when (it) {
                ScreenState.LOADING -> {
                    this.startActivityAndFinish(this, MainActivity::class.java)
                }

                ScreenState.ERROR -> shortShowToast("정보 갱신 실패")
                else -> shortShowToast("원인을 알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    private fun changeTeam() {
        val teamList = Organization.team.toTypedArray()

        var team = mainViewModel.team
        var index = teamList.indexOf(team)
        var builder = AlertDialog.Builder(this)
            .setTitle("팀 선택")
            .setSingleChoiceItems(teamList, index) { dialog, which ->
                team = teamList[which]
                binding.selectTeamBtn.text = team
            }
            .setPositiveButton("확인") { dialog, which ->
                mainViewModel.changeTeam(team)
            }

            .show()
    }

    private fun changePosition() {
        val positionList = Organization.position.toTypedArray()

        var position = mainViewModel.position
        val positionIdx = positionList.indexOf(position)
        Log.d("포지션${position}", "$positionIdx $positionList")
        var builder = AlertDialog.Builder(this)
            .setTitle("포지션 선택")
            .setSingleChoiceItems(positionList, positionIdx) { dialog, which ->
                position = positionList[which]
                binding.selectPositionBtn.text = position
            }.setPositiveButton("확인") { dialog, which ->
                mainViewModel.changePosition(position)
            }
            .show()
    }

    private fun logout(view: View) {
        splashViewModel.logout()
    }
}
