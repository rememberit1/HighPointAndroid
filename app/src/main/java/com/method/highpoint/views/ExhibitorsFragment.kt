package com.method.highpoint.views

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.adapters.ExhibitorAdapter
import com.method.highpoint.databinding.FragmentExhibitorsBinding
import com.method.highpoint.model.mymarket.MyMarketResponse
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.utils.getUserGUID
import com.method.highpoint.utils.isOnline
import com.method.highpoint.views.bottomsheet.BottomSheetFilter

class ExhibitorsFragment : Fragment() {

    private var _binding: FragmentExhibitorsBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: MainActivityViewModel
    private var buildingFilterName: String = ""
    private var filterId: Int = 0
    private var styleFilterId: Int = 0
    private var priceFilterId: Int = 0
    private var optionsFilterId: Int = 0
    private var areaFilterName: String = ""
    private var countryFilterName: String = ""
    private var filteredIds = ArrayList<Int>()

    val TAG = "ExhibitorFragment: "

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentExhibitorsBinding.inflate(inflater, container, false)
        buildingFilterName = arguments?.getString("BuildingFilterName") ?: ""
        areaFilterName = arguments?.getString("AreaFilterName") ?: ""
        countryFilterName = arguments?.getString("CountryFilterName") ?: ""
        styleFilterId = arguments?.getInt("StyleFilterId") ?: 0
        priceFilterId = arguments?.getInt("PriceFilterId") ?: 0
        optionsFilterId = arguments?.getInt("OptionsFilterId") ?: 0
        filterId = arguments?.getInt("FilterId") ?: 0
        if (styleFilterId != 0)
            filteredIds.add(styleFilterId)
        if (priceFilterId != 0)
            filteredIds.add(priceFilterId)
        if (optionsFilterId != 0)
            filteredIds.add(optionsFilterId)
        if (filterId != 0)
            filteredIds.add(filterId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listExhibitors.apply {
            layoutManager = LinearLayoutManager(activity)
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val adapter = ExhibitorAdapter(false)
        viewModel.fetchExhibitorFilters()

        checkFilterBadge()

        val userGUID = getUserGUID(requireActivity())
        if (!userGUID.isNullOrBlank()) {
            viewModel.fetchMyMarketDetails(userGUID)
        }
        binding.listExhibitors.adapter = adapter

        binding.filterLayout.setOnClickListener {
            BottomSheetFilter(
                buildingFilterName, filterId, styleFilterId,
            priceFilterId, optionsFilterId, areaFilterName,
            countryFilterName).show((context as FragmentActivity).supportFragmentManager, BottomSheetFilter().tag)
        }
        binding.filterButton.setOnClickListener {
            BottomSheetFilter(buildingFilterName, filterId, styleFilterId,
                priceFilterId, optionsFilterId, areaFilterName,
                countryFilterName).show((context as FragmentActivity).supportFragmentManager, BottomSheetFilter().tag)
        }

        // TODO remove below logic once QA confirms
//        if (buildingFilterName.isNullOrEmpty() && filterId == 0) {
//            activity?.let {
//                viewModel.exhibitorRoomData.observe(
//                    viewLifecycleOwner,
//                    Observer { exhibitorRoomData ->
//                        if (!getUserGUID(requireActivity()).isNullOrEmpty() && isOnline(
//                                requireContext()
//                            )
//                        ) {
//                            viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
//                                adapter.setExhibitors(
//                                    exhibitorRoomData.sortedBy { it.exhibitorName },
//                                    myMarketResponse ?: MyMarketResponse()
//                                )
//                            }
//                        } else {
//                            adapter.setExhibitors(
//                                exhibitorRoomData.sortedBy { it.exhibitorName },
//                                MyMarketResponse()
//                            )
//                        }
//                    }
//                )
//            }
//        }
//        else if (!buildingFilterName.isNullOrEmpty()) {
//            val filteredExhibitor = viewModel.getFilterExhibitorRoomData(buildingFilterName!!)
//            filteredExhibitor.observe(viewLifecycleOwner, Observer { filteredExhibitorData ->
//                if (!getUserGUID(requireActivity()).isNullOrEmpty()) {
//                    viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
//                        adapter.setExhibitors(
//                            filteredExhibitorData.sortedBy { it.exhibitorName },
//                            myMarketResponse ?: MyMarketResponse()
//                        )
//                    }
//                } else {
//                    adapter.setExhibitors(
//                        filteredExhibitorData.sortedBy { it.exhibitorName },
//                        MyMarketResponse()
//                    )
//                }
//            })
//        } else if (filterId != 0) {
//            viewModel.exhibitorRoomData.observe(viewLifecycleOwner) { exhibitorRoomData ->
//                val filteredExhibitor = ArrayList<ExhibitorRoomData>()
//                if (!getUserGUID(requireActivity()).isNullOrEmpty() && isOnline(requireContext())) {
//                    viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
//                        exhibitorRoomData.forEach { exhibitorData ->
//                            if (exhibitorData.filter?.contains(filterId) == true) {
//                                filteredExhibitor.add(exhibitorData)
//                            }
//                            if (myMarketResponse != null) {
//                                adapter.setExhibitors(filteredExhibitor.sortedBy { it.exhibitorName }, myMarketResponse)
//                            }
//                        }
//                    }
//                } else {
//                    exhibitorRoomData.forEach { exhibitorData ->
//                        val filteredExhibitor = ArrayList<ExhibitorRoomData>()
//                        if (exhibitorData.filter?.contains(filterId) == true) {
//                            filteredExhibitor.add(exhibitorData)
//                        }
//                        adapter.setExhibitors(
//                            exhibitorRoomData.sortedBy { it.exhibitorName },
//                            MyMarketResponse()
//                        )
//                    }
//                }
//            }
//        }

        if (binding.badgeFilter.visibility == View.GONE) {
            activity?.let {
                viewModel.exhibitorRoomData.observe(
                    viewLifecycleOwner,
                    Observer { exhibitorRoomData ->
                        if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                            viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
                                adapter.setExhibitors(
                                    exhibitorRoomData.sortedBy { it.exhibitorName },
                                    myMarketResponse ?: MyMarketResponse()
                                )
                            }
                        }else {
                            adapter.setExhibitors(
                                exhibitorRoomData.sortedBy { it.exhibitorName },
                                MyMarketResponse()
                            )
                        }
                    }
                )
            }
        } else {
            val filteredExhibitors = viewModel.getFilterExhibitorRoomData(countryFilterName, buildingFilterName, areaFilterName)
            if (filteredIds.isEmpty()) {
                filteredExhibitors.observe(viewLifecycleOwner, Observer { filteredExhibitorData ->
                    if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                        viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
                            adapter.setExhibitors(
                                filteredExhibitorData.sortedBy { it.exhibitorName },
                                myMarketResponse ?: MyMarketResponse()
                            )
                        }
                    } else {
                        adapter.setExhibitors(
                            filteredExhibitorData.sortedBy { it.exhibitorName },
                            MyMarketResponse()
                        )
                    }
                })
            } else {
                val allFiltered = ArrayList<ExhibitorRoomData>()
                filteredExhibitors.observe(viewLifecycleOwner) { filteredExhibitorData ->
                    filteredExhibitorData.forEach { filter ->
                        if (filter.filter?.containsAll(filteredIds) == true) {
                            allFiltered.add(filter)
                        }
                    }
                    if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                        viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
                            adapter.setExhibitors(
                                allFiltered.sortedBy { it.exhibitorName },
                                myMarketResponse ?: MyMarketResponse()
                            )
                        }
                    } else {
                        adapter.setExhibitors(
                            allFiltered.sortedBy { it.exhibitorName },
                            MyMarketResponse()
                        )
                    }
                }
            }
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val filteredExhibitors = viewModel.getExhibitorRoomData(query)
                    filteredExhibitors.observe(viewLifecycleOwner) { filteredExhibitorData ->
                        if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                            viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
                                adapter
                                adapter.setExhibitors(
                                    filteredExhibitorData.sortedBy { it.exhibitorName },
                                    myMarketResponse ?: MyMarketResponse()
                                )
                            }
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredExhibitors = viewModel.getExhibitorRoomData(newText)
                    filteredExhibitors.observe(viewLifecycleOwner) { filteredExhibitorData ->
                        if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                            viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
                                adapter.setExhibitors(
                                    filteredExhibitorData.sortedBy { it.exhibitorName },
                                    myMarketResponse ?: MyMarketResponse()
                                )
                            }
                        }
                    }
                }
                return true
            }

        })
    }
    private fun checkFilterBadge() {
        if (filterId != 0 || !buildingFilterName.isNullOrEmpty() || !areaFilterName.isNullOrEmpty()
            || !countryFilterName.isNullOrEmpty() || optionsFilterId != 0 || priceFilterId != 0 || styleFilterId != 0)
            binding.badgeFilter.visibility = View.VISIBLE
        else
            binding.badgeFilter.visibility = View.GONE
    }
}