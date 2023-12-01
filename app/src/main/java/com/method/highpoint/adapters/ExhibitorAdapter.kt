package com.method.highpoint.adapters

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.media.Image
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.model.mymarket.MyMarketResponse
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.repository.ApiRepository
import com.method.highpoint.utils.ButtonCommunicator
import com.method.highpoint.utils.ExhibitorsCommunicator
import com.method.highpoint.utils.getUserGUID
import com.method.highpoint.utils.isOnline
import com.method.highpoint.views.bottomsheet.BottomSheetExhibitorDetails
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExhibitorAdapter(val fromMyMarket: Boolean)
    : RecyclerView.Adapter<ExhibitorAdapter.ViewHolder>(),
        ExhibitorsCommunicator {

    private var exhibitorsData = ArrayList<ExhibitorRoomData>()
    private lateinit var mContext: Context
    private lateinit var bottomSheetExhibitorDetails: BottomSheetExhibitorDetails
    private var myMarketResponse = MyMarketResponse()
    private lateinit var apiRepository: ApiRepository

    inner class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var exhibitorNameTextView: TextView
        var locationDetailsTextView: TextView
        var userChipGroup: ChipGroup
        var star: ImageView
        var schedule: Chip
        var favorite: Chip

        init {
            exhibitorNameTextView = itemView.findViewById(R.id.exhibitor_name)
            locationDetailsTextView = itemView.findViewById(R.id.location_details)
            userChipGroup = itemView.findViewById(R.id.user_chip_group)
            schedule = itemView.findViewById(R.id.schedule)
            favorite = itemView.findViewById(R.id.favorite)
            star = itemView.findViewById(R.id.star)


            itemView.setOnClickListener { v: View -> var position: Int = adapterPosition
                bottomSheetExhibitorDetails =
                    BottomSheetExhibitorDetails(exhibitorsData[position], position , this@ExhibitorAdapter, myMarketResponse)
                bottomSheetExhibitorDetails.show((context as FragmentActivity).supportFragmentManager, bottomSheetExhibitorDetails.tag)
            }
            star.setOnClickListener { v: View -> var position = adapterPosition
                setIsRecyclable(true)
                if (isOnline(mContext)) {
                    if (v.tag.equals("") && !getUserGUID(mContext as Activity).isNullOrEmpty()) {
                        val myMarketStar = MyMarketStar(
                            userGuid = getUserGUID(mContext as Activity)!!,
                            itemType = "exhibitor",
                            itemTypeId = exhibitorsData[position].exhibitorId!!,
                            itemLove = false
                        )
                        apiRepository.postMyMarketItem(myMarketStar) {
                            if (it?.itemId != null) {
                                v.setBackgroundResource(R.drawable.ic_star_active)
                                v.tag = "selected"
                            }
                        }
                    } else {
                        val dialogBuilder = MaterialAlertDialogBuilder(mContext, R.style.HPDialogTheme)
                            .setTitle(R.string.remove_vendor)
                            .setMessage(R.string.remove_vendor_message)
                            .setNegativeButton(R.string.keep) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(R.string.remove) { dialog, _ ->
                                myMarketResponse.exhibitors?.forEach { myMarket ->
                                    if (myMarket?.itemTypeId == exhibitorsData[position].exhibitorId) {
                                        val itemId = myMarket?.itemId
                                        apiRepository.deleteMyMarket(
                                            getUserGUID(mContext as Activity)!!,
                                            itemId!!
                                        ) {
                                            dialog.dismiss()
                                            v.setBackgroundResource(R.drawable.ic_star_inactive)
                                            v.tag = ""
                                            if (fromMyMarket) {
                                                setIsRecyclable(true)
                                                exhibitorsData.removeAt(position)
                                                notifyItemRemoved(position)
                                            }
                                        }
                                    }
                                }
                            }
                            .show()
                        dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE)
                            .setTextColor(R.color.hp_primary)
                    }
                }
            }
            favorite.setOnCloseIconClickListener {
                if (isOnline(mContext)) {
                    val dialogBuilder = MaterialAlertDialogBuilder(mContext, R.style.HPDialogTheme)
                        .setTitle(R.string.remove_vendor)
                        .setMessage(R.string.remove_vendor_message)
                        .setNegativeButton(R.string.keep) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.remove) { dialog, _ ->
                            myMarketResponse.exhibitors?.forEach { myMarket ->
                                if (myMarket?.itemTypeId == exhibitorsData[position].exhibitorId) {
                                    val itemId = myMarket?.itemId
                                    val userGuid = getUserGUID(mContext as Activity)
                                    val myMarketStar = MyMarketStar(
                                        userGuid = userGuid!!,
                                        itemTypeId = myMarket?.itemTypeId!!,
                                        itemLove = false,
                                        itemType = "exhibitor",
                                        itemSchedule = myMarket.itemSchedule
                                    )
                                    apiRepository.putMyMarketItem(
                                        userGuid,
                                        itemId!!,
                                        myMarketStar
                                    ) {
                                        dialog.dismiss()
                                        favorite.visibility = View.GONE
                                    }
                                }
                            }
                        }
                        .show()
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setTextColor(R.color.hp_primary)
                }
            }
            schedule.setOnCloseIconClickListener {
                if (isOnline(mContext)) {
                    val dialogBuilder = MaterialAlertDialogBuilder(mContext, R.style.HPDialogTheme)
                        .setTitle(R.string.remove_vendor_visit)
                        .setMessage(R.string.remove_vendor_visit_message)
                        .setNegativeButton(R.string.keep) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.remove) { dialog, _ ->
                            myMarketResponse.exhibitors?.forEach { myMarket ->
                                if (myMarket?.itemTypeId == exhibitorsData[position].exhibitorId) {
                                    val itemId = myMarket?.itemId
                                    val userGuid = getUserGUID(mContext as Activity)
                                    val myMarketStar = MyMarketStar(
                                        userGuid = userGuid!!,
                                        itemTypeId = myMarket?.itemTypeId!!,
                                        itemLove = myMarket.itemLove!!,
                                        itemType = "exhibitor",
                                        itemSchedule = null
                                    )
                                    apiRepository.putMyMarketItem(
                                        userGuid,
                                        itemId!!,
                                        myMarketStar
                                    ) {
                                        dialog.dismiss()
                                        schedule.visibility = View.GONE
                                    }
                                }
                            }
                        }
                        .show()
                    dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setTextColor(R.color.hp_primary)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_exhibitors, parent, false)
        apiRepository = ApiRepository(context = mContext)
        return ViewHolder(view, mContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentExhibitor = exhibitorsData[position]
        holder.exhibitorNameTextView.text = currentExhibitor.exhibitorName
        if (currentExhibitor.buildingMultiTenant == true)
            holder.locationDetailsTextView.text = currentExhibitor.buildingName
        else
            holder.locationDetailsTextView.text = currentExhibitor.exhibitorShowroom
        holder.userChipGroup.removeAllViews()
        holder.setIsRecyclable(false)
        myMarketResponse.exhibitors?.forEach { myMarket ->
            if (currentExhibitor.exhibitorId == myMarket?.itemTypeId) {
                if (holder.star.tag.equals("") && currentExhibitor.exhibitorId == myMarket?.itemTypeId) {
                    holder.star.setBackgroundResource(R.drawable.ic_star_active)
                    holder.star.tag = "selected"
                }
                if (myMarket?.itemLove == true) {
                    holder.userChipGroup.addView(holder.favorite)
                    holder.userChipGroup.visibility = View.VISIBLE
                    holder.favorite.visibility = View.VISIBLE
                }
                if (!myMarket?.itemSchedule.isNullOrEmpty()) {
                    holder.userChipGroup.addView(holder.schedule)
                    holder.userChipGroup.visibility = View.VISIBLE
                    holder.schedule.visibility = View.VISIBLE
                    val date = LocalDateTime.parse(myMarket?.itemSchedule).format(DateTimeFormatter.ofPattern("EEE. d"))
                    holder.schedule.text = date
                }
                if (myMarket?.itemLove != true && myMarket?.itemSchedule.isNullOrEmpty()) {
                    holder.userChipGroup.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return exhibitorsData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setExhibitors(exhibitorsData: List<ExhibitorRoomData>, myMarketResponse: MyMarketResponse) {
        this.exhibitorsData = ArrayList(exhibitorsData)
        this.myMarketResponse = myMarketResponse
        notifyDataSetChanged()
    }

    override fun passExhibitorUpdates(position: Int, exhibitorRoomData: ExhibitorRoomData, myMarketResponse: MyMarketResponse) {
        this.myMarketResponse = myMarketResponse
        exhibitorsData.removeAt(position)
        exhibitorsData.add(position, exhibitorRoomData)
        notifyItemChanged(position)
        notifyDataSetChanged()
    }

}