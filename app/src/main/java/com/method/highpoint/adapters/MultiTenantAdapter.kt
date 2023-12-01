package com.method.highpoint.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.databinding.CardMultiTenantBinding
import com.method.highpoint.model.maps.BuildingExhibitorsItem

class MultiTenantAdapter : RecyclerView.Adapter<MultiTenantAdapter.ViewHolder>() {

    private lateinit var mContext: Context
    private var buildingExhibitorsItem = emptyList<BuildingExhibitorsItem>()

    inner class ViewHolder(val binding: CardMultiTenantBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding = CardMultiTenantBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return buildingExhibitorsItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val currentExhibitorItem = buildingExhibitorsItem[position]
            binding.exhibitorName.text = currentExhibitorItem.exhibitorName
            binding.floor.text = currentExhibitorItem.buildingFloor
        }
    }

    fun setupExhibitors(buildingExhibitorsItem: List<BuildingExhibitorsItem>) {
        this.buildingExhibitorsItem = buildingExhibitorsItem
        notifyDataSetChanged()
    }
}