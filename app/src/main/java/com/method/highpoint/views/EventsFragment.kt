package com.method.highpoint.views

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.adapters.EventsAdapter
import com.method.highpoint.databinding.FragmentEventsBinding
import com.method.highpoint.model.roomdb.event.EventRoomData
import com.method.highpoint.views.bottomsheet.BottomSheetFilter
import com.method.highpoint.views.bottomsheet.EventsBottomSheetFilter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class EventsFragment() : Fragment() {
    val TAG = "EventsFragment"
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    var eventMap = mutableMapOf<Int, String>()
    private lateinit var viewModel: MainActivityViewModel
    private var socialBool: Boolean? = null
    private var educationBool: Boolean? = null
    var horizontalSelfSelect = false
    lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)

        socialBool = arguments?.getBoolean("Social")
        educationBool = arguments?.getBoolean("Education")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.day1.isChecked = true
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            mLinearLayoutManager = layoutManager as LinearLayoutManager
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val adapter = EventsAdapter(eventMap, false)
        binding.recyclerView.adapter = adapter

        activity?.let {
            viewModel.eventRoomData.observe(
                viewLifecycleOwner
            ) { eventData ->
                if (socialBool == false && educationBool == false) {
                    //avoids app crashing onlick if there's no data
                    val emptyData = arrayListOf<EventRoomData>()
                    iterateAllDates(emptyData)
                    adapter.setEvents(emptyData)
                    activateCalendarClickListeners(false)
                } else if ((socialBool == true && educationBool == true) || (socialBool == null &&
                            educationBool == null)
                ) {
                    iterateAllDates(eventData.sortedBy { it.eventStartDateTime })
                    adapter.setEvents(eventData.sortedBy { it.eventStartDateTime })

                } else if (socialBool == true && educationBool == false) {
                    val filteredEvent = viewModel.getFilterEventRoomData(true)
                    filteredEvent.observe(viewLifecycleOwner, Observer { filteredEventData ->

                        iterateAllDates(filteredEventData.sortedBy { it.eventStartDateTime })
                        adapter.setEvents(filteredEventData.sortedBy { it.eventStartDateTime })
                    })
                } else {
                    val filteredEvent = viewModel.getFilterEventRoomData(false)
                    filteredEvent.observe(viewLifecycleOwner, Observer { filteredEventData ->
                        iterateAllDates(filteredEventData.sortedBy { it.eventStartDateTime })
                        adapter.setEvents(filteredEventData.sortedBy { it.eventStartDateTime })
                    })
                }
                activateScrollListener()
            }

            binding.filterButton.setOnClickListener {
                EventsBottomSheetFilter(socialBool, educationBool)
                    .show(
                    (context as FragmentActivity).supportFragmentManager,
                    BottomSheetFilter().tag
                )
            }
            activateCalendarClickListeners(true)
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val events = viewModel.getEventDataForQuery(query)
                    events.observe(viewLifecycleOwner, Observer { eventsData ->
                        iterateAllDates(eventsData.sortedBy { it.eventStartDateTime })
                        adapter.setEvents(eventsData.sortedBy { it.eventStartDateTime })

                    })
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val events = viewModel.getEventDataForQuery(newText)
                    events.observe(viewLifecycleOwner, Observer { eventsData ->
                        iterateAllDates(eventsData.sortedBy { it.eventStartDateTime })
                        adapter.setEvents(eventsData.sortedBy { it.eventStartDateTime })

                    })
                }
                return true
            }
        })

    }

    private fun activateCalendarClickListeners(activate: Boolean) {
        if (activate) {
            binding.day1.setOnClickListener {
                binding.recyclerView.smoothSnapToPosition(eventMap.keys.toTypedArray()[0])
            }
            binding.day2.setOnClickListener {
                binding.recyclerView.smoothSnapToPosition(eventMap.keys.toTypedArray()[1])
            }
            binding.day3.setOnClickListener {
                binding.recyclerView.smoothSnapToPosition(eventMap.keys.toTypedArray()[2])
            }
            binding.day4.setOnClickListener {
                binding.recyclerView.smoothSnapToPosition(eventMap.keys.toTypedArray()[3])
            }
            binding.day5.setOnClickListener {
                binding.recyclerView.smoothSnapToPosition(eventMap.keys.toTypedArray()[4])
            }
        } else {
            binding.day1.setOnClickListener {
            }
            binding.day2.setOnClickListener {
            }
            binding.day3.setOnClickListener {
            }
            binding.day4.setOnClickListener {
            }
            binding.day5.setOnClickListener {
            }
        }
    }

    private fun iterateAllDates(eventData: List<EventRoomData>) {
        iterateEventDates(eventData)
        if(eventMap.values.toTypedArray().size > 4) {
            binding.day5.visibility = View.VISIBLE
            binding.day4.visibility = View.VISIBLE
            binding.day3.visibility = View.VISIBLE
            binding.day2.visibility = View.VISIBLE
            binding.day1.visibility = View.VISIBLE
            binding.day5.text = eventMap.values.toTypedArray()[4]
            binding.day4.text = eventMap.values.toTypedArray()[3]
            binding.day3.text = eventMap.values.toTypedArray()[2]
            binding.day2.text = eventMap.values.toTypedArray()[1]
            binding.day1.text = eventMap.values.toTypedArray()[0]
        }else if (eventMap.values.toTypedArray().size == 4) {
            binding.day5.visibility = View.GONE
            binding.day4.visibility = View.VISIBLE
            binding.day3.visibility = View.VISIBLE
            binding.day2.visibility = View.VISIBLE
            binding.day1.visibility = View.VISIBLE
            binding.day4.text = eventMap.values.toTypedArray()[3]
            binding.day3.text = eventMap.values.toTypedArray()[2]
            binding.day2.text = eventMap.values.toTypedArray()[1]
            binding.day1.text = eventMap.values.toTypedArray()[0]
        }else if (eventMap.values.toTypedArray().size ==3) {
            binding.day4.visibility = View.GONE
            binding.day5.visibility = View.GONE
            binding.day3.visibility = View.VISIBLE
            binding.day2.visibility = View.VISIBLE
            binding.day1.visibility = View.VISIBLE
            binding.day3.text = eventMap.values.toTypedArray()[2]
            binding.day2.text = eventMap.values.toTypedArray()[1]
            binding.day1.text = eventMap.values.toTypedArray()[0]
        }else if (eventMap.values.toTypedArray().size == 2) {
            binding.day4.visibility = View.GONE
            binding.day5.visibility = View.GONE
            binding.day3.visibility = View.GONE
            binding.day1.visibility = View.VISIBLE
            binding.day2.visibility = View.VISIBLE
            binding.day2.text = eventMap.values.toTypedArray()[1]
            binding.day1.text = eventMap.values.toTypedArray()[0]
        }else if (eventMap.values.toTypedArray().size == 1) {
            binding.day4.visibility = View.GONE
            binding.day5.visibility = View.GONE
            binding.day3.visibility = View.GONE
            binding.day2.visibility = View.GONE
            binding.day1.visibility = View.VISIBLE
            binding.day1.text = eventMap.values.toTypedArray()[0]
        }
    }

    private fun activateScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
                val indexOfDaysList = eventMap.keys.toList()
                if (horizontalSelfSelect) {

                    if (indexOfDaysList.size > 4 && firstVisibleItem >= indexOfDaysList.last()) {
                        binding.day5.isChecked = true
                    }
                    else if (indexOfDaysList.size > 3 && firstVisibleItem < indexOfDaysList.last() && firstVisibleItem >= indexOfDaysList.get(
                            3
                        )
                    ) {
                        binding.day4.isChecked = true
                    } else if (indexOfDaysList.size > 2 && firstVisibleItem < indexOfDaysList.last() && firstVisibleItem >= indexOfDaysList.get(
                            2
                        )
                    ) {
                        binding.day3.isChecked = true
                    }  else if (indexOfDaysList.size > 1 && firstVisibleItem < indexOfDaysList.last() && firstVisibleItem >= indexOfDaysList.get(
                            1
                        )
                    ) {
                        binding.day2.isChecked = true
                    }
                    else if(indexOfDaysList.size == 1 || (indexOfDaysList.size > 1 &&firstVisibleItem < indexOfDaysList.get(1))){
                        binding.day1.isChecked = true
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        horizontalSelfSelect = true
                    }
                    RecyclerView.SCROLL_STATE_IDLE -> {
                            //Todo: save if necessary
                    }
                }
            }
        })
    }

    private fun RecyclerView.smoothSnapToPosition(
        position: Int,
        snapMode: Int = LinearSmoothScroller.SNAP_TO_START
    ) {
        horizontalSelfSelect = false
        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 1F / TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16F,
                    displayMetrics
                )
            }
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    //Sets the top calendar dates
    private fun iterateEventDates(events: List<EventRoomData>) {
        var dayToShow = ""
        eventMap.clear()
        for ((index, event) in events.withIndex()) {
            if (!dayToShow.equals(
                    LocalDateTime.parse(event.eventStartDateTime)
                        .format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}