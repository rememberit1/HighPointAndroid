package com.method.highpoint.views.bottomsheet

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.databinding.BottomEventsFilterBinding
import com.method.highpoint.views.EventsFragment

class EventsBottomSheetFilter(socialBoolean: Boolean?,
                              eduBoolean: Boolean?) : BottomSheetDialogFragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: BottomEventsFilterBinding

    private var mSocialBoolean: Boolean? = socialBoolean
    private var mEduBoolean: Boolean? = eduBoolean


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomEventsFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        if(mSocialBoolean!= null){
            binding.socialCheckbox.isChecked = mSocialBoolean!!
        } else {
            binding.socialCheckbox.isChecked = true
        }

        if(mEduBoolean!= null){
            binding.educationCheckbox.isChecked = mEduBoolean!!
        } else {
            binding.educationCheckbox.isChecked = true
        }

        binding.clearButton.setOnClickListener {
            binding.socialCheckbox.isChecked = false
            binding.educationCheckbox.isChecked = false
        }
        binding.resultsButton.setOnClickListener {
        recreateFragment()
            dismiss()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun recreateFragment(){
        mSocialBoolean = binding.socialCheckbox.isChecked
        mEduBoolean = binding.educationCheckbox.isChecked
        val nextFragment = EventsFragment()
        val args = Bundle()
        if((mSocialBoolean !=null) && (mEduBoolean != null)) {
            args.putBoolean("Social", mSocialBoolean!!)
            args.putBoolean("Education", mEduBoolean!!)
        }
        nextFragment.arguments = args

        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, nextFragment)?.commit()
    }

}