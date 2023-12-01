package com.method.highpoint.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.method.highpoint.R
import com.method.highpoint.databinding.BottomsheetMarketFilterBinding
import com.method.highpoint.views.MyMarketFragment
import com.method.highpoint.views.tabviews.AllMyMarketFragment

class BottomSheetMarketFilter(private val sort: String) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetMarketFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetMarketFilterBinding.inflate(inflater, container, false)
        when (sort) {
            "Alphabetical" -> binding.radioAlphabetical.isChecked = true
            "Building" -> binding.radioBuilding.isChecked = true
            "Neighborhood" -> binding.radioNeighborhood.isChecked = true
            "Shuttle Stop" -> binding.radioShuttleStop.isChecked = true
            else -> binding.radioAlphabetical.isChecked = true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetClose.setOnClickListener {
            dismiss()
        }

        binding.clearFilter.setOnClickListener {
            binding.radioAlphabetical.isChecked = true
        }

        binding.sort.setOnClickListener {
            if (binding.radioAlphabetical.isChecked) {
                callFragment(binding.radioAlphabetical.text.toString())
                dismiss()
            }
            if (binding.radioBuilding.isChecked) {
                callFragment(binding.radioBuilding.text.toString())
                dismiss()
            }
            if (binding.radioNeighborhood.isChecked) {
                callFragment(binding.radioNeighborhood.text.toString())
                dismiss()
            }
            if (binding.radioShuttleStop.isChecked) {
                callFragment(binding.radioShuttleStop.text.toString())
                dismiss()
            }
        }
    }

    private fun callFragment(sort: String) {
        val nextFragment = MyMarketFragment(sort)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, nextFragment)?.commit()
    }
}