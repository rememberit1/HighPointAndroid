package com.method.highpoint.adapters

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.method.highpoint.views.tabviews.AllMyMarketFragment
import com.method.highpoint.views.tabviews.MyEventsFragment

class ParentFragmentPagerAdapter(
    fragment: Fragment,
    val filter: String,
    val filterButton: ImageView,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                AllMyMarketFragment(filter, filterButton)
            }
            1 -> {
                MyEventsFragment(filterButton)
            }
            else -> {
                MyEventsFragment(filterButton)
            }
        }
    }
}