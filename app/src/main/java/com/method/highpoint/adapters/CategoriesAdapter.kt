package com.method.highpoint.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.method.highpoint.R
import com.method.highpoint.databinding.FilterChipBinding
import com.method.highpoint.databinding.RecyclerFilterLayoutBinding
import com.method.highpoint.model.category.CategoryModel
import com.method.highpoint.model.roomdb.category.CategoryRoomData
import com.method.highpoint.utils.Communicator
import com.method.highpoint.utils.FilterCommunicator

class CategoriesAdapter(
    val listener: FilterCommunicator,
    val filteredId: Int = 0
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var categoryData = emptyList<CategoryRoomData>()
    private lateinit var mContext: Context
    private var categoryParent = emptyList<String>()
    lateinit var chip: Chip
    private var filterId = filteredId

    inner class ViewHolder(val binding: RecyclerFilterLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val binding = RecyclerFilterLayoutBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.parentCategory.text = categoryParent[position]
            categoryData.forEach { category ->
                if (category.categoryParentName == categoryParent[position]) {
                    chip = category.categorySubName?.let { createChip(it) }!!
                    binding.filterChipGroup.addView(chip)
                    if (filteredId == category.categoryId) {
                        chip.isChecked = true
                    }
                    chip?.setOnCheckedChangeListener { _, isChecked ->
                        filterId = category.categoryId!!
                        listener.passFilters(filterId)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryParent.size
    }

    private fun createChip(label: String): Chip {
        val chip = FilterChipBinding.inflate(LayoutInflater.from(mContext)).root
        chip.text = label
        return chip
    }

    fun setupCategories(categoryData: List<CategoryRoomData>, categoryParent: List<String>) {
        this.categoryData = categoryData
        this.categoryParent = categoryParent
        notifyDataSetChanged()
    }
}