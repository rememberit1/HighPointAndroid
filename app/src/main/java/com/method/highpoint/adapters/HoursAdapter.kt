package com.method.highpoint.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.R
import com.method.highpoint.model.roomdb.hours.HoursRoomData

class HoursAdapter(private val mShowRoomHours: List<HoursRoomData>, private val mRowLayout: Int)
    : RecyclerView.Adapter<HoursAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(mRowLayout, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.timeWordsTextView.text = mShowRoomHours[position].description
        holder.timeNumbersTextView.text = mShowRoomHours[position].hours
    }

    override fun getItemCount(): Int {
        return mShowRoomHours.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeWordsTextView: TextView
        var timeNumbersTextView: TextView

        init {
            timeWordsTextView = itemView.findViewById(R.id.time_words_tv)
            timeNumbersTextView = itemView.findViewById(R.id.time_numbers_tv)
        }
    }



}