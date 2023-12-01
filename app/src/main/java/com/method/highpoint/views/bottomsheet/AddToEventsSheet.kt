package com.method.highpoint.views.bottomsheet

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.adapters.ExhibitorDateAdapter
import com.method.highpoint.databinding.AddToEventsSheetBinding
import com.method.highpoint.model.dates.MarketDate
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.repository.ApiRepository
import com.method.highpoint.utils.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class AddToEventsSheet(
    val exhibitorId: Int,
    val itemLove: Boolean,
    val itemNotes: String?,
    val listener: ButtonCommunicator
    ) : BottomSheetDialogFragment(), Communicator{
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: AddToEventsSheetBinding
    private var daysInEventList = ArrayList<String>()
    private var daysInEvent: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddToEventsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addEventRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.fetchDates()
        val adapter = ExhibitorDateAdapter(this@AddToEventsSheet)
        binding.addEventRecyclerView.adapter = adapter
        activity?.let {
            viewModel.dateModelLiveData?.observe(it, Observer { date ->
                val arrangedDates: List<MarketDate>? = date?.marketDates?.sortedBy { (it.eventDate) }
                arrangedDates?.forEach {
                    if(it.active){
                        daysInEvent = daysInBetween(it.openingDate, it.closingDate)
                        daysInEventList = selectionOfDaysToVisit(it)
                        adapter.setEvents(daysInEventList)
                        adapter.setEvents(selectionOfDaysToVisit(it))
                    }
                }
            })
        }
        binding.clearButton.setOnClickListener {
            dismiss()
        }
        binding.xButton.setOnClickListener {
            dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun daysInBetween(startDate: String, endDate:String): Int{
        val date1 = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(startDate.take(10))
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(endDate.take(10))
        val diff: Long = date2.time - date1.time
        return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1).toInt()
    }

    private fun addDateSuffix(date: String): String {
        val lastNumber = date.get(date.length-1)
        if(lastNumber.equals('1')){
            return date + "st"
        }else if(lastNumber.equals('2')){
            return date + "nd"
        } else  if (lastNumber.equals('3')){
            return date + "rd"
        } else {
            return date + "th"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addDays(dateData: String, numberOfDaysToAdd: Int): String {
        var dt = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateData.take(10))
        val c: Calendar = Calendar.getInstance()
        if (dt != null) {
            c.time = dt
        }
        c.add(Calendar.DATE, numberOfDaysToAdd)
        dt = c.time
        val f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu")
            .withLocale(Locale.US)
        val ld = ZonedDateTime.parse(dt.toString(), f).toLocalDate()
        val fLocalDate = DateTimeFormatter.ofPattern("eeee, MMMM dd")
        return ld.format(fLocalDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectionOfDaysToVisit(marketDate: MarketDate) : ArrayList<String>{
        daysInEventList.clear()
        for (i in 0 until daysInEvent){
            daysInEventList.add(addDateSuffix(addDays(marketDate.openingDate, i)))
        }
        return daysInEventList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDate(date: String): String {
        val year = Year.now().value
        var droppedThenAddedDate = date.dropLast(2)
        droppedThenAddedDate = "$droppedThenAddedDate $year"
        val formatter = DateTimeFormatter.ofPattern("eeee, MMMM dd yyyy", Locale.US)
        val dateTime = LocalDate.parse(droppedThenAddedDate, formatter)
        return dateTime.toString() + "T00:00:00"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun passData(date: String) {
        binding.resultsButton.setOnClickListener {
            if (isOnline(requireContext())) {
                val userGuid = getUserGUID(requireActivity())
                if (!userGuid.isNullOrEmpty()) {
                    val myMarketStar = MyMarketStar(
                        userGuid = userGuid,
                        itemTypeId = exhibitorId,
                        itemType = "exhibitor",
                        itemSchedule = convertToDate(date),
                        itemLove = itemLove,
                        itemNotes = itemNotes
                    )
                    ApiRepository(requireContext()).postMyMarketItem(myMarketStar) {
                        if (it?.itemId != null) {
                            Toast.makeText(requireContext(), "Added to Events!", Toast.LENGTH_SHORT).show()
                            listener.passRefreshData(true, false)
                            this.dismiss()
                        }
                    }
                } else {
                    showLoginAlert(requireContext())
                }
            } else {
                showConnectionAlert(requireContext())
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}