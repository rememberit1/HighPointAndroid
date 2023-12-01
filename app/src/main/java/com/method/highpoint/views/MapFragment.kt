package com.method.highpoint.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.databinding.FragmentMapBinding
import com.method.highpoint.utils.getUserGUID
import com.method.highpoint.utils.isOnline
import com.method.highpoint.views.bottomsheet.BottomSheetMaps
import com.method.highpoint.views.bottomsheet.BottomSheetMapsFilter
import java.util.*
import kotlin.collections.ArrayList

class MapFragment(val exhibitorFilter: String, val shuttleFilter: String) : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MainActivityViewModel
    private var shuttleListRed = ArrayList<LatLng>()
    private var shuttleListGreen = ArrayList<LatLng>()
    private lateinit var redPolyline: Polyline
    private lateinit var greenPolyline: Polyline
    private var marker1: Marker? = null
    var lastClickedMarker: Marker? = null

    companion object {
        var mapFragment : SupportMapFragment? = null
        private const val MY_PERMISSION_FINE_LOCATION = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterButton.setOnClickListener {
            BottomSheetMapsFilter(exhibitorFilter, shuttleFilter).show((context as FragmentActivity).supportFragmentManager, BottomSheetMapsFilter().tag)
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }
        else {//condition for Marshmello and above
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_FINE_LOCATION)
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.fetchShuttleDetails()
        viewModel.fetchMapLocations()
        viewModel.fetchShuttleLocationDetails()

        getUserGUID(requireActivity())?.let { viewModel.fetchMyMarketDetails(it) }
        getUserGUID(requireActivity())?.let { viewModel.fetchMyMapLocations(it) }

        greenPolyline = mMap.addPolyline(PolylineOptions())
        redPolyline = mMap.addPolyline(PolylineOptions())

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(35.956,-80.0032), 15f))

        val iconGenerator = IconGenerator(requireContext())
        iconGenerator.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ic_polygon_marker))


        viewModel.getShuttleData().observe(this, Observer { shuttleData ->
            shuttleData?.forEach { shuttle ->
                if (shuttle.line == 1 && (shuttleFilter.equals("All") || shuttleFilter.equals("Green"))) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(shuttle.latitude, shuttle.longitude))
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    iconGenerator.makeIcon(
                                        shuttle.stopNumber.toString()
                                    )
                                )
                            )
                            .title(shuttle.stopNumber.toString())
                        )
                } else if (shuttle.line == 2 && (shuttleFilter.equals("All") || shuttleFilter.equals("Red"))) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(shuttle.latitude, shuttle.longitude))
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    iconGenerator.makeIcon(
                                        shuttle.stopNumber.toString()
                                    )
                                )
                            )
                            .title(shuttle.stopNumber.toString())
                    )
                }
            }
            viewModel.getShuttleLines().observe(this, Observer { shuttleLines ->
                Log.d("ShuttleLines", shuttleLines.toString())
                shuttleLines?.forEach { shuttleLine ->
                    shuttleLine.shuttleLineLocations.forEach { shuttleLineLocation ->
                        if (shuttleLine.shuttleLine == 1 && (shuttleFilter.equals("All") || shuttleFilter.equals("Green"))) {
                            shuttleListGreen.add(
                                LatLng(
                                    shuttleLineLocation.latitude,
                                    shuttleLineLocation.longitude
                                )
                            )
                        } else if (shuttleLine.shuttleLine == 2 && (shuttleFilter.equals("All") || shuttleFilter.equals("Red"))) {
                            shuttleListRed.add(
                                LatLng(
                                    shuttleLineLocation.latitude,
                                    shuttleLineLocation.longitude
                                )
                            )
                        }
                    }
                }
                when (shuttleFilter) {
                    "All" -> {
                        displayGreenLine()
                        displayRedLine()
                    }
                    "Red" -> displayRedLine()
                    "Green" -> displayGreenLine()
                    "None" -> return@Observer
                }
            })
        })

        when(exhibitorFilter) {
            "All" -> {
                viewModel.getMapLocationsData().observe(this) { mapLocations ->
                    mapLocations?.forEach { location ->
                        val position = LatLng(location.latitude, location.longitude)
                        if (location.buildingId != null) {
                            marker1 = mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .icon(
                                        bitmapFromVector(
                                            requireContext(),
                                            R.drawable.ic_exhibitor_marker
                                        )
                                    )
                            )
                            marker1?.tag = location.buildingId
                        }
                        if (location.exhibitorId != null) {
                            marker1 = mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .icon(
                                        bitmapFromVector(
                                            requireContext(),
                                            R.drawable.ic_exhibitor_marker
                                        )
                                    )
                            )
                            marker1?.tag = location.exhibitorId
                        }
                    }
                }
            }
            "None" -> return
            "MyMarket" -> {
                if (!getUserGUID(requireActivity()).isNullOrBlank()) {
                    //TODO fetch logic with UserGuid
                    viewModel.isLoading().observe(this) { isLoading ->
                        if (isLoading) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.filterButton.isClickable = false
                        }
                        else {
                            binding.progressBar.visibility = View.GONE
                            binding.filterButton.isClickable = true
                        }
                        viewModel.myMapLocationsLiveData?.observe(this) { myMapLocations ->
                            myMapLocations?.forEach { mapLocations ->
                                val myPosition =
                                    LatLng(mapLocations.latitude, mapLocations.longitude)
                                if (mapLocations.buildingId != null) {
                                    marker1 = mMap.addMarker(
                                        MarkerOptions()
                                            .position(myPosition)
                                            .icon(
                                                bitmapFromVector(
                                                    requireContext(),
                                                    R.drawable.ic_exhibitor_marker
                                                )
                                            )
                                    )
                                    marker1?.tag = mapLocations.buildingId
                                } else if (mapLocations.exhibitorId != null) {
                                    marker1 = mMap.addMarker(
                                        MarkerOptions()
                                            .position(myPosition)
                                            .icon(
                                                bitmapFromVector(
                                                    requireContext(),
                                                    R.drawable.ic_exhibitor_marker
                                                )
                                            )
                                    )
                                    marker1?.tag = mapLocations.exhibitorId
                                }
                            }
                        }
                    }
                }
            }
        }

        //TODO delete once QA confirms no issues with testing
//        viewModel.getMapLocationsData().observe(this) { mapLocations ->
//            mapLocations?.forEach { location ->
//                val position = LatLng(location.latitude, location.longitude)
//                when (exhibitorFilter) {
//                    "All" -> {
//                        if (location.buildingId != null) {
//                            marker1 = mMap.addMarker(
//                                MarkerOptions()
//                                    .position(position)
//                                    .icon(
//                                        bitmapFromVector(
//                                            requireContext(),
//                                            R.drawable.ic_exhibitor_marker
//                                        )
//                                    )
//                            )
//                            marker1?.tag = location.buildingId
//                        }
//                        if (location.exhibitorId != null) {
//                            marker1 = mMap.addMarker(
//                                MarkerOptions()
//                                    .position(position)
//                                    .icon(
//                                        bitmapFromVector(
//                                            requireContext(),
//                                            R.drawable.ic_exhibitor_marker
//                                        )
//                                    )
//                            )
//                            marker1?.tag = location.exhibitorId
//                        }
//                    }
//                    "None" -> return@observe
//                    "MyMarket" -> {
//                        if (!getUserGUID(requireActivity()).isNullOrEmpty() && isOnline(requireContext())) {
//                            //TODO fetch logic with UserGuid
//                            viewModel.isLoading().observe(this) { isLoading ->
//                                if (isLoading) {
//                                    binding.progressBar.visibility = View.VISIBLE
//                                    binding.filterButton.isClickable = false
//                                }
//                                else {
//                                    binding.progressBar.visibility = View.GONE
//                                    binding.filterButton.isClickable = true
//                                }
//                                viewModel.myMapLocationsLiveData?.observe(this) { myMapLocations ->
//                                    myMapLocations?.forEach { mapLocations ->
//                                        val myPosition =
//                                            LatLng(mapLocations.latitude, mapLocations.longitude)
//                                        if (mapLocations.buildingId != null) {
//                                            marker1 = mMap.addMarker(
//                                                MarkerOptions()
//                                                    .position(myPosition)
//                                                    .icon(
//                                                        bitmapFromVector(
//                                                            requireContext(),
//                                                            R.drawable.ic_exhibitor_marker
//                                                        )
//                                                    )
//                                            )
//                                            marker1?.tag = mapLocations.buildingId
//                                        } else if (mapLocations.exhibitorId != null) {
//                                            marker1 = mMap.addMarker(
//                                                MarkerOptions()
//                                                    .position(myPosition)
//                                                    .icon(
//                                                        bitmapFromVector(
//                                                            requireContext(),
//                                                            R.drawable.ic_exhibitor_marker
//                                                        )
//                                                    )
//                                            )
//                                            marker1?.tag = mapLocations.exhibitorId
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        mMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            if (exhibitorFilter == "MyMarket") {
                viewModel.myMapLocationsLiveData?.observe(this) { mapLocations ->
                    mapLocations?.forEach { location ->
                        if (!marker.isInfoWindowShown && marker.title.isNullOrEmpty()) {
                            lastClickedMarker?.setIcon(
                                bitmapFromVector(
                                    requireContext(),
                                    R.drawable.ic_exhibitor_marker
                                )
                            )
                            marker.setIcon(
                                bitmapFromVector(
                                    requireContext(),
                                    R.drawable.ic_exhibitor_selected
                                )
                            )
                            if (marker.tag == location.buildingId || marker.tag == location.exhibitorId) {
                                var tenant = ""
                                lastClickedMarker = marker
                                if (location.exhibitorId != null)
                                    tenant = "Single tenant"
                                else if (location.buildingId != null)
                                    tenant = "Multi-tenant"
                                val address = if (location.buildingId != null) {
                                    viewModel.getBuildingAddress(location.buildingId.toString())
                                } else {
                                    viewModel.getExhibitorAddress(location.exhibitorId.toString())
                                }
                                BottomSheetMaps(
                                    tenant,
                                    address,
                                    location.buildingExhibitors,
                                    location.buildingName
                                ).show((context as FragmentActivity).supportFragmentManager, tag)
                            }
                        }
                    }
                }
            } else {
            viewModel.getMapLocationsData().observe(this) { mapLocations ->
                mapLocations?.forEach { location ->
                    if (!marker.isInfoWindowShown && marker.title.isNullOrEmpty()) {
                        lastClickedMarker?.setIcon(
                            bitmapFromVector(
                                requireContext(),
                                R.drawable.ic_exhibitor_marker
                            )
                        )
                        marker.setIcon(
                            bitmapFromVector(
                                requireContext(),
                                R.drawable.ic_exhibitor_selected
                            )
                        )
                        if (marker.tag == location.buildingId || marker.tag == location.exhibitorId) {
                            var tenant = ""
                            lastClickedMarker = marker
                            if (location.exhibitorId != null)
                                tenant = "Single tenant"
                            else if (location.buildingId != null)
                                tenant = "Multi-tenant"
                            val address = if (location.buildingId != null) {
                                viewModel.getBuildingAddress(location.buildingId.toString())
                            } else {
                                viewModel.getExhibitorAddress(location.exhibitorId.toString())
                            }
                            BottomSheetMaps(
                                tenant,
                                address,
                                location.buildingExhibitors,
                                location.buildingName
                            ).show((context as FragmentActivity).supportFragmentManager, tag)
                        }
                    }
                }
            }}
            return@OnMarkerClickListener false
        })
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)
        vectorDrawable.draw(Canvas(bitmap))
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun displayGreenLine() {
        greenPolyline = mMap.addPolyline(
            PolylineOptions()
                .addAll(shuttleListGreen)
                .width(5F)
                .color(Color.GREEN)
                .visible(true)
                .geodesic(true)
        )
    }

    private fun displayRedLine() {
        redPolyline = mMap.addPolyline(
            PolylineOptions()
                .addAll(shuttleListRed)
                .width(5F)
                .color(Color.RED)
                .visible(true)
                .geodesic(true)
        )
    }

}