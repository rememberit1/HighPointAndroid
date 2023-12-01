package com.method.highpoint.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.R
import com.method.highpoint.model.dates.MarketDate
import com.method.highpoint.model.roomdb.dates.DatesRoomData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateAdapter(private val mMarketDate: List<DatesRoomData>, private val mRowLayout: Int
) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(mRowLayout, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder:  DateAdapter.ViewHolder, position: Int) {
        var startDate = LocalDateTime.parse(mMarketDate[position].openingDate).format(DateTimeFormatter
            .ofPattern("MMMM dd"))
        var endDate = LocalDateTime.parse(mMarketDate[position].closingDate).format(DateTimeFormatter
            .ofPattern("dd"))
        holder.timeWordsTextView.text = mMarketDate[position].marketName
        holder.timeNumbersTextView.text = "${addDateSuffix(startDate)} - ${addDateSuffix(endDate)}"

    }


    private fun addDateSuffix(date: String): String {
        var lastNumber = date.get(date.length-1)
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

    override fun getItemCount(): Int {
        return mMarketDate.size
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