package com.method.highpoint.views.tabviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.adapters.ExhibitorAdapter
import com.method.highpoint.databinding.FragmentAllMyMarketBinding
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.utils.getUserGUID

class AllMyMarketFragment(
    val sort: String,
    val filterButton: ImageView
) : Fragment() {

    private lateinit var binding: FragmentAllMyMarketBinding
    private lateinit var viewModel: MainActivityViewModel
    private var exhibitorsData = mutableListOf<ExhibitorRoomData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllMyMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.myMarketRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        fetchDetails()

        val adapter = ExhibitorAdapter(true)
        binding.myMarketRecycler.adapter = adapter

        viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
            myMarketResponse?.exhibitors?.forEach { exhibitorsItem ->
                if (exhibitorsItem?.itemTypeId != null) {
                    binding.myMarketRecycler.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                    val filteredExhibitors = viewModel.getMyMarket(exhibitorsItem.itemTypeId)
                    filteredExhibitors.observe(viewLifecycleOwner) { filteredExhibitorData ->
                        filteredExhibitorData.forEach { filteredExhibitor ->
                            exhibitorsData.add(filteredExhibitor)
                        }
                        when (sort) {
                            "Alphabetical" -> adapter.setExhibitors(
                                exhibitorsData.sortedBy { it.exhibitorName },
                                myMarketResponse
                            )
                            "default" -> adapter.setExhibitors(
                                exhibitorsData.sortedBy { it.exhibitorName },
                                myMarketResponse
                            )
                            "Building" -> adapter.setExhibitors(
                                exhibitorsData.sortedBy { it.buildingName },
                                myMarketResponse
                            )
                            "Neighborhood" -> adapter.setExhibitors(
                                exhibitorsData.sortedBy { it.exhibitorNeighborhood },
                                myMarketResponse
                            )
                            "Shuttle Stop" -> adapter.setExhibitors(
                                exhibitorsData.sortedBy { it.exhibitorBustop },
                                myMarketResponse
                            )
                        }
                    }

                }
            }
            if (myMarketResponse?.exhibitors?.isEmpty() == true) {
                binding.myMarketRecycler.visibility = View.GONE
                binding.emptyState.visibility = View.VISIBLE
            }
        }

    }

    private fun fetchDetails() {
        viewModel.fetchExhibitorFilters()
        val userGUID = getUserGUID(requireActivity())
        if (!userGUID.isNullOrBlank()) {
            viewModel.fetchMyMarketDetails(userGUID)
        }
    }

    override fun onResume() {
        super.onResume()
        filterButton.isClickable = true
    }
}