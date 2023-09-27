package com.ncc.presentation.view.main.auth


import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ncc.domain.utils.ScreenState
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentLoginBinding
import com.ncc.presentation.view.main.MainActivity
import com.ncc.presentation.view.splash.startActivityAndFinish
import com.ncc.presentation.viewmodel.MainViewModel
import com.ncc.presentation.viewmodel.SplashViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val splashViewModel by viewModels<SplashViewModel>()
    private var id = ""
    private var password = ""
    override fun init() {
        binding.fragment = this

    }

//    fun loginBtn(view: View) {
//        id = binding.id.text.toString()
//        password = binding.password.text.toString()
//        splashViewModel.login(id, password).addOnSuccessListener { authResult ->
//            //로그인 성공
//            this.findNavController().navigate(R.id.splashActivity)
//        }
//            //로그인 실패
//            .addOnFailureListener { exception ->
//                val error = exception.message.toString()
//                shortShowToast(error)
//            }
//    }
}
//
////intent and finish
//fun AppCompatActivity.startActivityAndFinish(context: Context, activity: Class<*> ){
//    startActivity(Intent(context,activity).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//    this.finish()
//}