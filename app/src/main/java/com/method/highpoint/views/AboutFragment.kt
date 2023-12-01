package com.method.highpoint.views

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.OnBoardingActivity
import com.method.highpoint.R
import com.method.highpoint.adapters.DateAdapter
import com.method.highpoint.adapters.HoursAdapter
import com.method.highpoint.databinding.FragmentAboutBinding
import com.method.highpoint.model.roomdb.dates.DatesRoomData
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AboutFragment : Fragment() {

    val TAG = "AboutFragment"
    //to ensure binding is set in null when view is destroyed
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.experienceButton.setOnClickListener{goToSite(getString(R.string.experience_url))}
        binding.thingsToDo.setOnClickListener{goToSite(getString(R.string.experience_url))}
        binding.privacyButton.setOnClickListener{goToSite(getString(R.string.privacy_site))}
        binding.horizontalCardLayout.setOnClickListener{goToSite(getString(R.string.privacy_site))}

        collapseAndExpand(binding.relativeCardView, binding.whereLayout, binding.whereHighPointButton)
        collapseAndExpand(binding.gpsRelativeTitle, binding.gpsCardView, binding.gpsButton)
        collapseAndExpand(binding.marketRelativeLayout, binding.marketPassCardView, binding.marketButton)
        collapseAndExpand(binding.registerTitleView, binding.registerGetThereCard, binding.registerButton)
        collapseAndExpand(binding.showroomTitleRelative, binding.showroomHoursCardView, binding.showroomHoursButton)
        collapseAndExpand(binding.sundayRelative, binding.openSundayCardView, binding.openSundayButton)
        collapseAndExpand(binding.shopRelativeTitle, binding.shopCardView, binding.shopButton)
        collapseAndExpand(binding.airportTitleRelative, binding.shuttlesCardView, binding.shuttleButton)
        collapseAndExpand(binding.parkRelative, binding.parkingCardView, binding.parkingLotsButton)
        collapseAndExpand(binding.socialRelativeView, binding.socialCardView, binding.socialButton)
        collapseAndExpand(binding.relativePhotoCard, binding.photoCardView, binding.photoButton)
        collapseAndExpand(binding.questionsRelativeView, binding.questionsCardView, binding.questionButton)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getDatesRoomData()
        viewModel.getHoursRoomData()
        activity?.let {
            viewModel.hoursRoomLiveData?.observe(it, Observer { hours ->//change this to hours
                binding.hoursRecycler.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = HoursAdapter( hours, R.layout.about_list_item)
                }
            })
            viewModel.datesRoomLiveData?.observe(it, Observer { dates ->
                binding.datesRecycler.apply {
                    layoutManager = LinearLayoutManager(activity)
                    val arrangedDates: List<DatesRoomData> = dates.sortedBy { (it.eventDate) }
                    daysInBetween(arrangedDates[0].openingDate, arrangedDates[0].closingDate)
                    adapter = DateAdapter(arrangedDates, R.layout.about_list_item)
                }
            })
        }

        binding.hoursRecycler.addItemDecoration(DividerItemDecoration(binding.hoursRecycler.context,
                DividerItemDecoration.VERTICAL))
        binding.datesRecycler.addItemDecoration(DividerItemDecoration(binding.datesRecycler.context,
            DividerItemDecoration.VERTICAL))

        binding.tutorialLayout.setOnClickListener {
            val intent = Intent(activity, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.tutorial.setOnClickListener {
            val intent = Intent(activity, OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun daysInBetween(startDate: String, endDate:String): Int{
        var date1 = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(startDate.take(10))
        var date2 = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(endDate.take(10))
        var diff: Long = date2.time - date1.time
        return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1).toInt()
    }

    fun collapseAndExpand(titleLayout: RelativeLayout, cardLayout: LinearLayout, imageButton: ImageView){
        titleLayout.setOnClickListener{
            if(cardLayout.visibility == View.GONE){
                cardLayout.visibility = View.VISIBLE
                imageButton.rotation = 45F
            }else{
                cardLayout.visibility = View.GONE
                imageButton.rotation = 0F
            }
            cardLayout.setOnClickListener {
                cardLayout.visibility = View.GONE
                imageButton.rotation = 0F
            }
        }
    }

    private fun goToSite(site: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(site))
        startActivity(browserIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}