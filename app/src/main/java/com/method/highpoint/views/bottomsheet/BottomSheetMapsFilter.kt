package com.method.highpoint.views.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.method.highpoint.R
import com.method.highpoint.databinding.BottomsheetMapsFilterBinding
import com.method.highpoint.views.MapFragment

class BottomSheetMapsFilter(
    val exhibitorFilter: String = "All",
    val routeFilter: String = "All"
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetMapsFilterBinding
    var exhibitor: String = exhibitorFilter
    var routes: String = routeFilter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetMapsFilterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomsheetClose.setOnClickListener {
            dismiss()
        }
        when (exhibitorFilter) {
            "All" -> binding.allExhibitors.isChecked = true
            "MyMarket" -> binding.myMarket.isChecked = true
            "None" -> binding.none.isChecked = true
        }
        when (routeFilter) {
            "All" -> binding.allRoutes.isChecked = true
            "Red" -> binding.redLine.isChecked = true
            "Green" -> binding.greenLine.isChecked = true
            "None" -> binding.none.isChecked = true
        }
        binding.clear.setOnClickListener {
            binding.allExhibitors.isChecked = false
            binding.myMarket.isChecked = false
            binding.none.isChecked = false
            binding.allRoutes.isChecked = false
            binding.redLine.isChecked = false
            binding.greenLine.isChecked = false
            binding.noneLine.isChecked = false
            exhibitor = "All"
            routes = "All"
        }
        binding.allExhibitors.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.myMarket.isChecked = false
                binding.none.isChecked = false
                exhibitor = "All"
            }
        }
        binding.myMarket.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allExhibitors.isChecked = false
                binding.none.isChecked = false
                exhibitor = "MyMarket"
            }
        }
        binding.none.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allExhibitors.isChecked = false
                binding.myMarket.isChecked = false
                exhibitor = "None"
            }
        }
        binding.allRoutes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.redLine.isChecked = false
                binding.greenLine.isChecked = false
                binding.noneLine.isChecked = false
                routes = "All"
            }
        }
        binding.redLine.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allRoutes.isChecked = false
                binding.greenLine.isChecked = false
                binding.noneLine.isChecked = false
                routes = "Red"
            }
        }
        binding.greenLine.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allRoutes.isChecked = false
                binding.redLine.isChecked = false
                binding.noneLine.isChecked = false
                routes = "Green"
            }
        }
        binding.noneLine.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allRoutes.isChecked = false
                binding.redLine.isChecked = false
                binding.greenLine.isChecked = false
                routes = "None"
            }
        }

        binding.showResults.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, MapFragment(exhibitor, routes))
                ?.commit()
            dismiss()
        }
    }
}