package com.method.highpoint.adapters

import com.method.highpoint.R
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.method.highpoint.utils.Communicator


class ExhibitorDateAdapter(private val listener: Communicator) : RecyclerView.Adapter<ExhibitorDateAdapter.ViewHolder>() {
    var mSelectedItem = -1
    private var selectableDates =  emptyList<String>()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExhibitorDateAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_add_to_events, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder:  ExhibitorDateAdapter.ViewHolder, position: Int) {
        holder.mRadio.setChecked(position == mSelectedItem);
        holder.dateTextView.text = selectableDates[position]
    }

    override fun getItemCount(): Int {
        return selectableDates.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setEvents(eventsDates: List<String>) {
        this.selectableDates = eventsDates
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            var mRadio: RadioButton
            var dateTextView: TextView

            init {
                dateTextView = itemView.findViewById(R.id.text_view_add_event)
                mRadio = itemView.findViewById<View>(R.id.radio_add_event) as RadioButton

                itemView.setOnClickListener(this)
                mRadio.setOnClickListener(this)
            }

        override fun onClick(p0: View?) {
            mSelectedItem = adapterPosition
            notifyItemRangeChanged(0, selectableDates.size)
            val stringDate = selectableDates[mSelectedItem]
            if (mSelectedItem != RecyclerView.NO_POSITION) {
                listener.passData(stringDate)
            }
        }
    }

}