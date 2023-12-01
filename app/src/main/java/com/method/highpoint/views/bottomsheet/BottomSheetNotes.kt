package com.method.highpoint.views.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.databinding.BottomsheetNotesBinding
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.model.roomdb.exhibitor.ExhibitorRoomData
import com.method.highpoint.repository.ApiRepository
import com.method.highpoint.utils.ButtonCommunicator
import com.method.highpoint.utils.getUserGUID
import com.method.highpoint.utils.isOnline

class BottomSheetNotes(
    val exhibitorName: String,
    val exhibitorId: Int,
    private val isEditNote: Boolean,
    val exhibitorDetails: ExhibitorRoomData,
    val listener: ButtonCommunicator
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetNotesBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetNotesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        getUserGUID(requireActivity())?.let { viewModel.fetchMyMarketDetails(it) }
        if (isEditNote) {
            binding.buttonClear.visibility = View.GONE
            binding.buttonDelete.visibility = View.VISIBLE
            viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarket ->
                myMarket?.exhibitors?.forEach { exhibitor ->
                    if (exhibitor?.itemTypeId == exhibitorId) {
                        binding.notes.setText(exhibitor.itemNotes)
                    }
                }
            }
        } else {
            binding.buttonClear.visibility = View.VISIBLE
            binding.buttonDelete.visibility = View.GONE
        }

        binding.exhibitorName.text = exhibitorName
        binding.buttonSave.setOnClickListener {
            if (isOnline(requireContext()) &&
                !getUserGUID(requireActivity()).isNullOrBlank() &&
                !binding.notes.text.isNullOrEmpty()) {
                viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarket ->
                    var itemLove = false
                    var itemSchedule: String? = null
                    myMarket?.exhibitors?.forEach { exhibitor ->
                        if (exhibitor?.itemTypeId == exhibitorId) {
                            itemLove = exhibitor.itemLove ?: false
                            itemSchedule = exhibitor.itemSchedule
                        }
                    }
                    val myMarketStar = MyMarketStar(
                        userGuid = getUserGUID(requireActivity())!!,
                        itemTypeId = exhibitorId,
                        itemType = "exhibitor",
                        itemLove = itemLove,
                        itemSchedule = itemSchedule,
                        itemNotes = binding.notes.text.toString()
                    )
                    ApiRepository(requireContext()).postMyMarketItem(myMarketStar) {
                        if (it?.itemId != null) {
                            Toast.makeText(requireContext(), "Note Created!", Toast.LENGTH_SHORT).show()
                            listener.passRefreshData(false, true)
                            this.dismiss()
                        }
                    }
                }
            }
        }
        binding.buttonClear.setOnClickListener {
            binding.notes.setText("")
        }
        binding.closeButton.setOnClickListener {
            this.dismiss()
            listener.passRefreshData(false, true)
        }
        binding.buttonDelete.setOnClickListener {
            val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.HPDialogTheme)
                .setTitle(R.string.delete_notes)
                .setMessage(R.string.delete_description)
                .setNegativeButton(R.string.keep_note) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.delete_notes) { v, _ ->
                    viewModel.myMarketResponseLiveData?.observe(viewLifecycleOwner) { myMarket ->
                        myMarket?.exhibitors?.forEach { exhibitor ->
                            val myMarketStar = MyMarketStar(
                                userGuid = getUserGUID(requireActivity())!!,
                                itemTypeId = exhibitorId,
                                itemLove = exhibitor?.itemLove!!,
                                itemNotes = "",
                                itemType = "exhibitor"
                            )
                            if (exhibitor?.itemTypeId == exhibitorId) {
                                ApiRepository(requireContext()).putMyMarketItem(
                                    getUserGUID(requireActivity())!!,
                                    exhibitor.itemId!!,
                                    myMarketStar
                                ) {
                                    v.dismiss()
                                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                                    listener.passRefreshData(false, true)
                                    this.dismiss()
                                }
                            }
                        }
                    }
                }
                .show()
            dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(R.color.hp_primary)
        }
    }
}