package com.ncc.presentation.view.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseActivity
import com.ncc.presentation.databinding.ActivityAdminBinding
import com.ncc.presentation.view.main.MainActivity
import com.ncc.presentation.view.main.auth.LoginActivity
import com.ncc.presentation.view.main.handover.HandoverFragment
import com.ncc.presentation.view.main.routine.RoutineFragment
import com.ncc.presentation.view.splash.startActivityAndFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : BaseActivity<ActivityAdminBinding>(R.layout.activity_admin) {

    private val adminFragment = AdminFragment()
    private val writeRoutineFragment = WriteRoutineFragment()

    private var isFragment = true
    override fun init() {
        binding.adminGoMain.setOnClickListener {
            this.startActivityAndFinish(this, MainActivity::class.java)
        }
        fragmentNavigation(adminFragment)
        binding.changeFragment.setOnClickListener {
            toggleFragment()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun toggleFragment() {
        if (isFragment) {
            binding.changeFragment.text = "routine 작성"
            fragmentNavigation(writeRoutineFragment)
        } else {
            binding.changeFragment.text = "관리 페이지로"
            fragmentNavigation(adminFragment)
        }
        isFragment = !isFragment // 플래그 업데이트
    }


    private fun fragmentNavigation(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_admin, fragment)
            .commit()
    }
}