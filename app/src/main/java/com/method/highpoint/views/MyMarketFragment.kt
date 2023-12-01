package com.method.highpoint.views

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.adapters.ParentFragmentPagerAdapter
import com.method.highpoint.databinding.FragmentMyMarketBinding
import com.method.highpoint.views.bottomsheet.BottomSheetMarketFilter
import com.method.highpoint.views.signin.LoginFragment

class MyMarketFragment(val sort: String) : Fragment() {

    private var _binding: FragmentMyMarketBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val pagerAdapter = ParentFragmentPagerAdapter(this, sort, binding.filterButton)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            val tabNames = listOf("All", "My Events")
            tab.text = tabNames[position]
        }.attach()

        binding.filterButton.setOnClickListener {
            BottomSheetMarketFilter(sort).show((context as FragmentActivity).supportFragmentManager, tag)
        }

        binding.logoutButton.setOnClickListener {
            val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.HPDialogTheme)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_description)
                .setPositiveButton(R.string.logout) { dialog, _ ->
                    val sharedPreference = activity?.getPreferences(Context.MODE_PRIVATE)
                    with(sharedPreference?.edit()) {
                        this?.putString("UserGUID", "")
                        this?.apply()
                    }
                    dialog.dismiss()
                    replaceFragment(LoginFragment())
                }
                .setNegativeButton(R.string.go_back) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(
                R.color.hp_primary))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
        fragmentTransaction?.commit()
    }

}
