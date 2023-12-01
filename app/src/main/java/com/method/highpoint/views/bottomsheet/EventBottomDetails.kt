package com.method.highpoint.views.bottomsheet

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.adapters.EventsAdapter
import com.method.highpoint.databinding.BottomEventsDetailBinding
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.model.roomdb.event.EventRoomData
import com.method.highpoint.repository.ApiRepository
import com.method.highpoint.utils.*
import com.method.highpoint.views.EventsFragment
import com.method.highpoint.views.tabviews.MyEventsFragment
import java.util.*

class EventBottomDetails(
    private var eventDetails: EventRoomData,
    private var formattedTime: String,
    private val position: Int,
    private val listener: EventsCommunicator
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomEventsDetailBinding
    private lateinit var bottomSheetShare: BottomSheetShare
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomEventsDetailBinding.inflate(inflater, container, false)
        setDataContents()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        getUserGUID(requireActivity())?.let { viewModel.fetchMyMarketDetails(it) }

        if (!getUserGUID(requireActivity()).isNullOrBlank()) {
            viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarket ->
                myMarket?.events?.forEach { event ->
                    if (event?.itemTypeId == eventDetails.eventId) {
                        binding.scheduleButton.visibility = View.GONE
                        binding.removeSchedule.visibility = View.VISIBLE
                    }
                }
            }
        }



        binding.closeButton.setOnClickListener {
            this.dismiss()
        }

        binding.shareButton.setOnClickListener {
            bottomSheetShare = BottomSheetShare("https://www.highpointmarket.org/events/")
            bottomSheetShare.show(
                (context as FragmentActivity).supportFragmentManager,
                bottomSheetShare.tag
            )
        }

        binding.scheduleButton.setOnClickListener {
            if (isOnline(requireContext())) {
                if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                    val myMarketStar = MyMarketStar(
                        userGuid = getUserGUID(requireActivity())!!,
                        itemTypeId = eventDetails.eventId!!,
                        itemType = "event",
                        itemLove = false
                    )
                    ApiRepository(requireContext()).postMyMarketItem(myMarketStar) {
                        if (it?.itemId != null) {
                            dialog?.window?.decorView?.let { it ->
                                Snackbar.make(it, R.string.event_added, Snackbar.LENGTH_SHORT)
                                    .show()
                            }
                            binding.scheduleButton.visibility = View.GONE
                            binding.removeSchedule.visibility = View.VISIBLE
                            listener.passEventUpdates(position, false, eventDetails)
                        }
                    }
                } else {
                    showLoginAlert(requireContext())
                }
            } else {
                showConnectionAlert(requireContext())
            }
        }

        binding.removeSchedule.setOnClickListener {
            if (isOnline(requireContext())) {
                val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.HPDialogTheme)
                    .setTitle(R.string.remove_event)
                    .setMessage(R.string.remove_event_message)
                    .setNegativeButton(R.string.close) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.remove_event) { v, _ ->
                        viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarket ->
                            myMarket?.events?.forEach { event ->
                                if (event?.itemTypeId == eventDetails.eventId) {
                                    ApiRepository(requireContext()).deleteMyMarket(
                                        getUserGUID(requireActivity())!!,
                                        itemId = event?.itemId!!
                                    ) {
                                        binding.scheduleButton.visibility = View.VISIBLE
                                        binding.removeSchedule.visibility = View.GONE
                                        listener.passEventUpdates(position, true, eventDetails)
                                    }
                                }
                            }
                        }
                        v.dismiss()
                    }
                    .show()
                dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(R.color.hp_primary)
            } else {
                showConnectionAlert(requireContext())
            }
        }
    }

    private fun setDataContents() {
        binding.eventTitle.text = eventDetails.eventTitle
        showOrHideDataContents(binding.eventTitle.text as String, binding.eventTitle)
        binding.eventTimeTv.text = formattedTime
        showOrHideDataContents( binding.eventTimeTv.text as String, binding.eventTimeTv)
        binding.eventTypeTv.text = eventDetails.eventType
        showOrHideDataContents(binding.eventTypeTv.text as String, binding.eventTypeTv)
        binding.eventLocationTv.text = eventDetails.eventLocation
        showOrHideDataContents(binding.eventLocationTv.text as String, binding.eventLocationTv)
        binding.eventAboutTv.text = eventDetails.eventSummary
        showOrHideDataContents(binding.eventAboutTv.text as String, binding.eventAboutTv)
    }

    private fun showOrHideDataContents(textEmptyOrNot: String, textView: TextView){
        if(textEmptyOrNot.isEmpty()){
            textView.visibility = View.GONE
        }
    }

}
