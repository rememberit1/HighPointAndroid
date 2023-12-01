package com.method.highpoint.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.R
import com.method.highpoint.model.roomdb.event.EventRoomData
import com.method.highpoint.utils.EventsCommunicator
import com.method.highpoint.views.bottomsheet.EventBottomDetails
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventsAdapter(
    var eventsMap: Map<Int, String>,
    val fromMyMarket: Boolean
) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>(),
    EventsCommunicator {
    private var mEventModels = ArrayList<EventRoomData>()
    private lateinit var eventBottomDetails: EventBottomDetails
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_events, parent, false)
        mContext = parent.context
        return ViewHolder(view, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.eventAddressTextView.text = mEventModels[position].eventLocation
        holder.eventTitleTextView.text = mEventModels[position].eventTitle

        val startTime = LocalDateTime.parse(mEventModels[position].eventStartDateTime).format(
            DateTimeFormatter.ofPattern("h:mma")
        )
        val endTime = LocalDateTime.parse(mEventModels[position].eventEndDateTime).format(
            DateTimeFormatter.ofPattern("h:mma")
        )
        holder.timeTextView.text = "$startTime - $endTime"
        if (eventsMap.containsKey(position)) {

            if(position == 0) {
                holder.divider.visibility = View.INVISIBLE
            }else{
                holder.divider.visibility = View.VISIBLE
            }

            val dayDividerDay = LocalDateTime.parse(mEventModels[position].eventEndDateTime).format(
                DateTimeFormatter.ofPattern("EEEE, MMMM d")
            )
            holder.dayDividerTextView.visibility = View.VISIBLE
            holder.dayDividerTextView.text = dayDividerDay
        } else {
            holder.divider.visibility = View.GONE
            holder.dayDividerTextView.visibility = View.GONE
        }
        holder.cardEventView.setOnClickListener {
            eventBottomDetails = EventBottomDetails(
                mEventModels[position], "$startTime - $endTime", position, this
            )
            eventBottomDetails.show(
                (mContext as FragmentActivity).supportFragmentManager,
                eventBottomDetails.tag
            )
        }
    }

    override fun getItemCount(): Int {
        return mEventModels.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setEvents(eventsData: List<EventRoomData>) {
        this.mEventModels = ArrayList(eventsData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var eventTitleTextView: TextView
        var eventAddressTextView: TextView
        var timeTextView: TextView
        var dayDividerTextView: TextView
        var typeTextView: TextView
        var cardEventView: CardView
        var divider: View

        init {
            eventTitleTextView = itemView.findViewById(R.id.event_title)
            eventAddressTextView = itemView.findViewById(R.id.event_address)
            timeTextView = itemView.findViewById(R.id.time)
            dayDividerTextView = itemView.findViewById(R.id.day_divider)
            typeTextView = itemView.findViewById(R.id.type_name)
            divider = itemView.findViewById(R.id.divider)
            cardEventView = itemView.findViewById(R.id.card_view_item)
            cardEventView.setOnClickListener {
                adapterPosition
            }
        }
    }

    override fun passEventUpdates(position: Int, removed: Boolean, eventRoomData: EventRoomData) {
        if (fromMyMarket) {
            if (removed) {
                mEventModels.removeAt(position)
                notifyItemChanged(position)
                notifyDataSetChanged()
            } else {
                mEventModels.add(position, eventRoomData)
                notifyItemInserted(position)
                notifyDataSetChanged()
            }
        }
    }

}
