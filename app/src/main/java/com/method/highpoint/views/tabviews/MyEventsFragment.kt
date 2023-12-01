package com.method.highpoint.views.tabviews

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.adapters.EventsAdapter
import com.method.highpoint.databinding.FragmentMyEventsBinding
import com.method.highpoint.model.roomdb.event.EventRoomData
import com.method.highpoint.utils.getUserGUID
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyEventsFragment(val filterButton: ImageView) : Fragment() {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var viewModel: MainActivityViewModel
    var eventMap = mutableMapOf<Int, String>()
    private var eventsData = mutableListOf<EventRoomData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.eventsRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        fetchDetails()

        val adapter = EventsAdapter(eventMap, true)
        binding.eventsRecycler.adapter = adapter

        viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarketResponse ->
            myMarketResponse?.events?.forEach { eventsItem ->
                if (eventsItem?.itemTypeId != null) {
                    binding.eventsRecycler.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                    val filteredEvents = viewModel.getMyEvents(eventsItem.itemTypeId)
                    filteredEvents.observe(viewLifecycleOwner) { filteredEventsData ->
                        filteredEventsData.forEach { filteredEvents ->
                            eventsData.add(filteredEvents)
                        }
                        iterateEventDates(eventsData.sortedBy { it.eventStartDateTime })
                        adapter.setEvents(eventsData.sortedBy { it.eventStartDateTime })
                    }
                }
            }
            if (myMarketResponse?.events?.isEmpty() == true) {
                binding.eventsRecycler.visibility = View.GONE
                binding.emptyState.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun iterateEventDates(events: List<EventRoomData>) {
        var dayToShow = ""
        eventMap.clear()
        for ((index, event) in events.withIndex()) {
            if (!dayToShow.equals(
                    LocalDateTime.parse(event.eventStartDateTime)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
            ) {
                dayToShow = LocalDateTime.parse(event.eventStartDateTime)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                eventMap.set(
                    index,
                    LocalDateTime.parse(event.eventStartDateTime)
                        .format(DateTimeFormatter.ofPattern("EEE. d"))
                )
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
        filterButton.isClickable = false
    }
}