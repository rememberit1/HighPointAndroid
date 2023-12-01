package com.method.highpoint.views.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.adapters.CategoriesAdapter
import com.method.highpoint.databinding.BottomsheetFilterBinding
import com.method.highpoint.databinding.FilterChipBinding
import com.method.highpoint.model.roomdb.category.CategoryRoomData
import com.method.highpoint.utils.FilterCommunicator
import com.method.highpoint.views.ExhibitorsFragment

class BottomSheetFilter(
    buildingFilteredName: String? = "",
    filteredId: Int = 0,
    styledFilteredId: Int = 0,
    priceFilteredId: Int = 0,
    optionsFilteredId: Int = 0,
    areaFilteredName: String? = "",
    countryFilteredName: String? = "",
) : BottomSheetDialogFragment(), FilterCommunicator {

    private lateinit var binding: BottomsheetFilterBinding
    private lateinit var viewModel: MainActivityViewModel
    private var buildingFilterName: String? = buildingFilteredName
    private var filterId: Int = filteredId
    private var styleFilterId: Int = styledFilteredId
    private var priceFilterId: Int = priceFilteredId
    private var optionsFilterId: Int = optionsFilteredId
    private var areaFilterName: String? = areaFilteredName
    private var countryFilterName: String? = countryFilteredName

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetFilterBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog)?.behavior?.state = STATE_EXPANDED
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        //setting up adapters
        val categoriesAdapter = CategoriesAdapter(this@BottomSheetFilter, filterId)
        binding.categoryRecyclerView.adapter = categoriesAdapter

        activity?.let {
            viewModel.getCategoryData().observe(
                viewLifecycleOwner,
                Observer { categoriesData ->
                    val arr = listOf(categoriesData)
                    val distParent = arr.flatten().mapNotNull { (it as? CategoryRoomData)?.categoryParentName }.distinct()
                    categoriesAdapter.setupCategories(categoriesData, distParent)
                }
            )
            viewModel.getStyleData().observe(
                viewLifecycleOwner,
                Observer { styleData ->
                    styleData.forEach { style ->
                        val chip = style.styleName?.let { createChip(it) }
                        binding.styleChipGroup.addView(chip)
                        if (styleFilterId == style.styleId) {
                            chip?.isChecked = true
                        }
                        chip?.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                styleFilterId = style.styleId
                            }
                        }
                    }
                }
            )
            viewModel.getPricePointData().observe(
                viewLifecycleOwner,
                Observer { pricePointData ->
                    pricePointData.forEach { pricePoint ->
                        val chip = pricePoint.pricePointName?.let {
                            createChip(it)
                        }
                        binding.priceChipGroup.addView(chip)
                        if (priceFilterId == pricePoint.pricePointId) {
                            chip?.isChecked = true
                        }
                        chip?.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                priceFilterId = pricePoint.pricePointId!!
                            }
                        }
                    }
                }
            )
            viewModel.getBuildingData().observe(
                viewLifecycleOwner,
                Observer { buildingData ->
                    buildingData.forEach { building ->
                        val chip = building.buildingName?.let { createChip(it) }
                        binding.buildingChipGroup.addView(chip)
                        if (buildingFilterName == building.buildingName) {
                            chip?.isChecked = true
                        }
                        chip?.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                buildingFilterName = chip.text.toString()
                            }
                        }
                    }
                }
            )

            viewModel.getOptionData().observe(
                viewLifecycleOwner,
                Observer { optionsData ->
                    optionsData.forEach { options ->
                        val chip = options.optionName?.let { createChip(it) }
                        binding.optionsChipGroup.addView(chip)
                        if (optionsFilterId == options.optionId) {
                            chip?.isChecked = true
                        }
                        chip?.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                binding.countryChipGroup.clearCheck()
                                binding.areasChipGroup.clearCheck()
                                binding.priceChipGroup.clearCheck()
                                binding.styleChipGroup.clearCheck()
                                binding.buildingChipGroup.clearCheck()
                                optionsFilterId = options.optionId
                            }
                        }
                    }
                }
            )

            viewModel.getAreaData().observe(
                viewLifecycleOwner,
                Observer { areaData ->
                    areaData.forEach { area ->
                        val chip = area.exhibitorAreaInterest?.let { createChip(it) }
                        binding.areasChipGroup.addView(chip)
                        if (areaFilterName == area.exhibitorAreaInterest) {
                            chip?.isChecked = true
                        }
                        chip?.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                areaFilterName = chip.text.toString()
                            }
                        }
                    }
                }
            )

            viewModel.getCountryData().observe(
                viewLifecycleOwner,
                Observer { countriesData ->
                    countriesData.forEach { country ->
                        val chip = country.exhibitorCountry?.let { createChip(it) }
                        binding.countryChipGroup.addView(chip)
                        if (countryFilterName == country.exhibitorCountry) {
                            chip?.isChecked = true
                        }
                        chip?.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                countryFilterName = chip.text.toString()
                            }
                        }
                    }
                }
            )
        }

        binding.bottomsheetClose.setOnClickListener {
            dismiss()
        }

        binding.resetFilters.setOnClickListener {
            clearChip()
            callExhibitorFragment()
            dismiss()
        }

        binding.showResults.setOnClickListener {
            callExhibitorFragment()
            dismiss()
        }
    }

    private fun callExhibitorFragment() {
        val nextFragment = ExhibitorsFragment()
        val args = Bundle()
        args.putString("BuildingFilterName", buildingFilterName)
        args.putString("AreaFilterName", areaFilterName)
        args.putString("CountryFilterName", countryFilterName)
        args.putInt("FilterId", filterId)
        args.putInt("StyleFilterId", styleFilterId)
        args.putInt("PriceFilterId", priceFilterId)
        args.putInt("OptionsFilterId", optionsFilterId)
        nextFragment.arguments = args
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.fragment_container, nextFragment)
            ?.commit()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, ExhibitorsFragment())
    }

    private fun setupViews() {
        collapseAndExpandRecycler(binding.categoryLayout, binding.categoryRecyclerView)
        collapseAndExpand(binding.styleLayout, binding.styleChipGroup)
        collapseAndExpand(binding.pricePointLayout, binding.priceChipGroup)
        collapseAndExpand(binding.buildingLayout, binding.buildingChipGroup)
        collapseAndExpand(binding.optionsLayout, binding.optionsChipGroup)
        collapseAndExpand(binding.areasLayout, binding.areasChipGroup)
        collapseAndExpand(binding.countryLayout, binding.countryChipGroup)
    }

    private fun collapseAndExpandRecycler(titleLayout: RelativeLayout, recyclerView: RecyclerView) {
        titleLayout.setOnClickListener {
            if (recyclerView.visibility == View.GONE) {
                recyclerView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.GONE
            }
            recyclerView.setOnClickListener {
                recyclerView.visibility = View.GONE
            }
        }
    }

    private fun collapseAndExpand(titleLayout: RelativeLayout, chipGroup: ChipGroup) {
        titleLayout.setOnClickListener {
            if (chipGroup.visibility == View.GONE) {
                chipGroup.visibility = View.VISIBLE
            } else {
                chipGroup.visibility = View.GONE
            }
            chipGroup.setOnClickListener {
                chipGroup.visibility = View.GONE
            }
        }
    }

    private fun createChip(label: String): Chip {
        val chip = FilterChipBinding.inflate(LayoutInflater.from(context)).root
        chip.text = label
        return chip
    }

    private fun clearChip() {
        binding.countryChipGroup.clearCheck()
        binding.areasChipGroup.clearCheck()
        binding.priceChipGroup.clearCheck()
        binding.optionsChipGroup.clearCheck()
        binding.styleChipGroup.clearCheck()
        binding.buildingChipGroup.clearCheck()
        buildingFilterName = ""
        countryFilterName = ""
        areaFilterName = ""
        filterId = 0
        optionsFilterId = 0
        priceFilterId = 0
        styleFilterId = 0
    }

    override fun passFilters(filterId: Int) {
        this.filterId = filterId
    }
}