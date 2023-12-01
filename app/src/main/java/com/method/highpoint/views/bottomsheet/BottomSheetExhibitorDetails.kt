package com.method.highpoint.views.bottomsheet


import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.method.highpoint.BuildConfig
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.databinding.BottomsheetExhibitorDetailsBinding
import com.method.highpoint.model.mymarket.MyMarketResponse
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.repository.ApiRepository
import com.method.highpoint.utils.*
import com.squareup.picasso.Picasso
import java.util.*


class BottomSheetExhibitorDetails(
    private val exhibitorDetails: ExhibitorRoomData,
    val position: Int,
    val listener: ExhibitorsCommunicator,
    val myMarketResponse: MyMarketResponse
) : BottomSheetDialogFragment(), ButtonCommunicator {

    private lateinit var binding: BottomsheetExhibitorDetailsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetShare: BottomSheetShare
    private lateinit var bottomSheetNotes: BottomSheetNotes
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var apiRepository: ApiRepository
    var mapUrl: String = ""

    companion object {
        var mapFragment : SupportMapFragment? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetExhibitorDetailsBinding.inflate(inflater, container, false)
        //TODO remove after QA confirms
//        mapFragment = childFragmentManager.findFragmentById(R.id.map_location) as? SupportMapFragment
//        mapFragment?.getMapAsync(this)
        apiRepository = ApiRepository(requireContext())
        setDataContents()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.bottomsheetClose.setOnClickListener {
            this.dismiss()
        }

        binding.website.setOnClickListener {
            exhibitorDetails.exhibitorWebsite?.let { websiteURL -> goToSite(websiteURL) }
        }

        binding.share.setOnClickListener {
            bottomSheetShare = BottomSheetShare(getString(R.string.share_url, exhibitorDetails.exhibitorId.toString()))
            bottomSheetShare.show((context as FragmentActivity).supportFragmentManager, bottomSheetShare.tag)
        }

        binding.facebook.setOnClickListener {
            exhibitorDetails.exhibitorFacebook?.let { facebookURL -> goToSite(facebookURL) }
        }

        binding.twitter.setOnClickListener {
            exhibitorDetails.exhibitorTwitter?.let { twitterURL -> goToSite(twitterURL) }
        }

        binding.youtube.setOnClickListener {
            exhibitorDetails.exhibitorYoutube?.let { youtubeURL -> goToSite(youtubeURL) }
        }

        binding.instagram.setOnClickListener {
            exhibitorDetails.exhibitorInstagram?.let { instagramURL -> goToSite(instagramURL) }
        }

        binding.pintrest.setOnClickListener {
            exhibitorDetails.exhibitorPinterest?.let { pintrestURL -> goToSite(pintrestURL) }
        }

        binding.navigate.setOnClickListener {
            if (exhibitorDetails.buildingAddress != null) {
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=${exhibitorDetails.buildingAddress}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
        val userGUID = getUserGUID(requireActivity())
        if (!userGUID.isNullOrBlank() && isOnline(requireContext())) {
            viewModel.fetchMyMarketDetails(userGUID)
        }

        refreshContents()

        binding.addNote.setOnClickListener {
            if (isOnline(requireContext())) {
                if (!userGUID.isNullOrEmpty()) {
                    bottomSheetNotes = BottomSheetNotes(
                        exhibitorDetails.exhibitorName,
                        exhibitorDetails.exhibitorId!!,
                        false,
                        exhibitorDetails,
                        this@BottomSheetExhibitorDetails
                    )
                    bottomSheetNotes.show(
                        (context as FragmentActivity).supportFragmentManager,
                        bottomSheetNotes.tag
                    )
                } else {
                    showLoginAlert(requireContext())
                }
            } else {
                showConnectionAlert(requireContext())
            }
        }

        binding.editNote.setOnClickListener {
            if (isOnline(requireContext())) {
                bottomSheetNotes = BottomSheetNotes(
                    exhibitorDetails.exhibitorName,
                    exhibitorDetails.exhibitorId!!,
                    true,
                    exhibitorDetails,
                    this@BottomSheetExhibitorDetails
                )
                bottomSheetNotes.show(
                    (context as FragmentActivity).supportFragmentManager,
                    bottomSheetNotes.tag
                )
            } else {
                showConnectionAlert(requireContext())
            }
        }

        binding.editNoteButton.setOnClickListener {
            if (isOnline(requireContext())) {
                bottomSheetNotes = BottomSheetNotes(
                    exhibitorDetails.exhibitorName,
                    exhibitorDetails.exhibitorId!!,
                    true,
                    exhibitorDetails,
                    this@BottomSheetExhibitorDetails
                )
                bottomSheetNotes.show(
                    (context as FragmentActivity).supportFragmentManager,
                    bottomSheetNotes.tag
                )
            } else {
                showConnectionAlert(requireContext())
            }
        }

        binding.favorite.setOnClickListener {
            if (isOnline(requireContext())) {
                if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                    var itemSchedule: String? = null
                    var itemNotes: String? = null
                    viewModel.myMarketResponseLiveData?.observe(this) { myMarketResponse ->
                        myMarketResponse?.exhibitors?.forEach { exhibitor ->
                            if (exhibitor?.itemTypeId == exhibitorDetails.exhibitorId) {
                                itemSchedule = exhibitor?.itemSchedule
                                itemNotes = exhibitor?.itemNotes
                            }
                        }
                        val myMarketStar = MyMarketStar(
                            userGuid = getUserGUID(requireActivity())!!,
                            itemTypeId = exhibitorDetails.exhibitorId!!,
                            itemType = "exhibitor",
                            itemLove = true,
                            itemSchedule = itemSchedule,
                            itemNotes = itemNotes
                        )
                        apiRepository.postMyMarketItem(myMarketStar) {
                            if (it?.itemId != null && it.itemLove) {
                                binding.favoriteAdded.visibility = View.VISIBLE
                                binding.favorite.visibility = View.GONE
                                dialog?.window?.decorView?.let { it ->
                                    Snackbar.make(
                                        it,
                                        R.string.added_to_favorites,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } else {
                    showLoginAlert(requireContext())
                }
            } else {
                showConnectionAlert(requireContext())
            }
        }

        binding.schedule.setOnClickListener {
            if (isOnline(requireContext())) {
                viewModel.myMarketResponseLiveData?.observe(this) { myMarketResponse ->
                    var itemLove: Boolean = false
                    var itemNotes: String? = null
                    myMarketResponse?.exhibitors?.forEach { exhibitor ->
                        if (exhibitor?.itemTypeId == exhibitorDetails.exhibitorId) {
                            itemLove = exhibitor?.itemLove ?: false
                            itemNotes = exhibitor?.itemNotes
                        }
                    }
                    AddToEventsSheet(exhibitorDetails.exhibitorId!!, itemLove, itemNotes, this@BottomSheetExhibitorDetails).show(
                        (context as FragmentActivity).supportFragmentManager,
                        "AddToEventsSheet"
                    )
                }
            } else {
                showConnectionAlert(requireContext())
            }
        }

    }

    private fun setDataContents() {
        binding.exhibitorTitle.text = exhibitorDetails.exhibitorName
        if (exhibitorDetails.exhibitorWebsite.isNullOrEmpty()) {
            binding.website.visibility = View.GONE
        }
        if (exhibitorDetails.exhibitorFacebook.isNullOrEmpty()) {
            binding.facebook.visibility = View.GONE
        }
        if (exhibitorDetails.exhibitorTwitter.isNullOrEmpty()) {
            binding.twitter.visibility = View.GONE
        }
        if (exhibitorDetails.exhibitorYoutube.isNullOrEmpty()) {
            binding.youtube.visibility = View.GONE
        }
        if (exhibitorDetails.exhibitorInstagram.isNullOrEmpty()) {
            binding.instagram.visibility = View.GONE
        }
        if (exhibitorDetails.exhibitorPinterest.isNullOrEmpty()) {
            binding.pintrest.visibility = View.GONE
        }

        if (exhibitorDetails.buildingMultiTenant == true) {
            binding.location.text = exhibitorDetails.buildingName + " - " +
                    exhibitorDetails.exhibitorShowroom + " ," + exhibitorDetails.buildingFloor
        } else
            binding.location.text = exhibitorDetails.exhibitorShowroom

        binding.shuttleStop.text = exhibitorDetails.exhibitorBustop.toString() + " ( ${exhibitorDetails.exhibitorBustopType} )"
        binding.neighborhood.text = exhibitorDetails.exhibitorNeighborhood
        binding.websiteText.text = exhibitorDetails.exhibitorWebsite
        binding.aboutDescription.text = exhibitorDetails.exhibitorDescription

        try {
            val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
            val address: String? = if (exhibitorDetails.buildingMultiTenant == true) {
                exhibitorDetails.buildingAddress
            } else
                exhibitorDetails.exhibitorShowroom

            val addList = address?.let { geocoder?.getFromLocationName(it, 1) }
            val lat = addList?.get(0)?.latitude ?: 35.955700
            val lng = addList?.get(0)?.longitude ?: -80.005630
            mapUrl =
                "https://maps.googleapis.com/maps/api/staticmap?center=$lat,$lng" +
                        "&zoom=15&size=380x264&markers=color:green|$lat,$lng" +
                        "&key="+BuildConfig.GOOGLE_API_KEY

            Picasso.get()
                .load(mapUrl)
                .error(R.drawable.ic_maps_fallback)
                .into(binding.mapLocation)

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            val lat = 35.955700
            val lng = -80.005630
            mapUrl =
                "https://maps.googleapis.com/maps/api/staticmap?center=$lat,$lng" +
                        "&zoom=15&size=380x264&markers=color:green|$lat,$lng" +
                        "&key="+BuildConfig.GOOGLE_API_KEY
            Picasso.get()
                .load(mapUrl)
                .placeholder(R.drawable.ic_maps_fallback)
                .error(R.drawable.ic_maps_fallback)
                .into(binding.mapLocation)
        }
    }

    private fun goToSite(site: String) {
        val browserIntent = if (site.contains("http"))
            Intent(Intent.ACTION_VIEW, Uri.parse(site))
        else
            Intent(Intent.ACTION_VIEW, Uri.parse("http://$site"))
        startActivity(browserIntent)
    }

    //TODO remove after QA confirms
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        //Convert address to lat lng for map
//        if (isOnline(requireContext())) {
//            val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
//            val address: String? = if (exhibitorDetails.buildingMultiTenant == true) {
//                exhibitorDetails.buildingAddress
//            } else
//                exhibitorDetails.exhibitorShowroom
//
//            val addList = address?.let { geocoder?.getFromLocationName(it, 1) }
//            val lat = addList?.get(0)?.latitude
//            val lng = addList?.get(0)?.longitude
//            if (lat != null && lng != null) {
//                val position = LatLng(lat, lng)
//                mMap.addMarker(MarkerOptions().position(position))
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15F))
//            }
//        }
//    }

    override fun passRefreshData(fromSchedule: Boolean, fromNotes: Boolean) {
        if (fromNotes || fromSchedule) {
            refreshContents()
        }
    }

    private fun refreshContents() {
        val userGUID = getUserGUID(requireActivity())
        if (!userGUID.isNullOrBlank()) {
            viewModel.fetchMyMarketDetails(userGUID)
        }
        viewModel.myMarketResponseLiveData?.observe(this, Observer { myMarketResponse ->
            myMarketResponse?.exhibitors?.forEach { exhibitorItem ->
                if (!exhibitorItem?.itemNotes.isNullOrEmpty() && exhibitorItem?.itemTypeId == exhibitorDetails.exhibitorId) {
                    binding.notes.visibility = View.VISIBLE
                    binding.notesDetails.text = exhibitorItem?.itemNotes
                    binding.addNote.visibility = View.GONE
                    binding.editNoteButton.visibility = View.VISIBLE
                }
                if (exhibitorItem?.itemLove == true && exhibitorItem.itemTypeId == exhibitorDetails.exhibitorId) {
                    binding.favorite.visibility = View.GONE
                    binding.favoriteAdded.visibility = View.VISIBLE
                    binding.favorite.isClickable = false
                }
                if (exhibitorItem?.itemSchedule != null && exhibitorItem.itemTypeId == exhibitorDetails.exhibitorId) {
                    binding.schedule.visibility = View.GONE
                    binding.scheduleAdded.visibility = View.VISIBLE
                    binding.schedule.isClickable = false
                }
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.passExhibitorUpdates(position, exhibitorDetails, myMarketResponse)
    }
}