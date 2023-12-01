package com.method.highpoint.views.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.method.highpoint.adapters.MultiTenantAdapter
import com.method.highpoint.databinding.BottomsheetMapsBinding
import com.method.highpoint.model.maps.BuildingExhibitorsItem

class BottomSheetMaps(
    val tenant: String,
    val address: String?,
    val buildingExhibitors: List<BuildingExhibitorsItem>?,
    val buildingName: String
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.exhibitorsRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
        }
        val adapter = MultiTenantAdapter()
        binding.exhibitorsRecycler.adapter = adapter

        if (!tenant.equals("Single tenant")) {
            binding.multiTenantView.visibility = View.VISIBLE
            if (buildingExhibitors != null) {
                adapter.setupExhibitors(buildingExhibitors)
            }
        }

        binding.bottomsheetClose.setOnClickListener {
            dismiss()
        }

        if (tenant.equals("Single tenant")) {
            buildingExhibitors?.forEach { buildingExhibitorsItem ->
                binding.tenantText.text = buildingName
                binding.shuttleStopText.text = buildingExhibitorsItem.shuttleStopNumber.toString()
            }
        } else {
            binding.tenantText.text = buildingName
            buildingExhibitors?.forEach { buildingExhibitorsItem ->
                binding.shuttleStopText.text = buildingExhibitorsItem.shuttleStopNumber.toString()
            }
        }

        binding.address.text = address

        binding.navigateButton.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${address}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}