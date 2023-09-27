package com.ncc.presentation.view.main.handover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ncc.presentation.R
import com.ncc.presentation.base.BaseFragment
import com.ncc.presentation.databinding.FragmentDetailHandoverBinding
import com.ncc.presentation.databinding.FragmentHandoverBinding
import com.ncc.presentation.databinding.FragmentSelectPartBinding
import com.ncc.presentation.databinding.FragmentWriteHandoverBinding
import com.ncc.presentation.viewmodel.MainViewModel


//class WriteHandoverFragment :
//    BaseFragment<FragmentWriteHandoverBinding>(R.layout.fragment_write_handover) {
//    private val mainViewModel by activityViewModels<MainViewModel>()
//    override fun init() {
//        binding.fragment = this
//    }
//
//    fun WriteHandoverBtn(view: View) {
//        binding.loadingBar.visibility = View.VISIBLE
//        mainViewModel.handoverTitle = binding.titleEdit.text.toString()
//        mainViewModel.handoverRoutine = binding.routineEdit.text.toString()
//        mainViewModel.handoverContent = binding.contentEdit.text.toString()
//
//    }
//}
class WriteHandoverFragment :
    BaseFragment<FragmentWriteHandoverBinding>(R.layout.fragment_write_handover) {
    private val mainViewModel by activityViewModels<MainViewModel>()
    override fun init() {
        binding.fragment = this
    }

//    fun WriteHandoverBtn(view: View) {
//        binding.loadingBar.visibility = View.VISIBLE
//        mainViewModel.handoverTitle = binding.titleEdit.text.toString()
//        mainViewModel.handoverRoutine = binding.routineEdit.text.toString()
//        mainViewModel.handoverContent = binding.contentEdit.text.toString()
//    }
}
