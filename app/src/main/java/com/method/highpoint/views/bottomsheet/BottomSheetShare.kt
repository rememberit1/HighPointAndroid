package com.method.highpoint.views.bottomsheet

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.method.highpoint.R
import com.method.highpoint.databinding.BottomsheetShareBinding

class BottomSheetShare(private val shareLink: String) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetShareBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetShareBinding.inflate(inflater, container, false)

        setDataContents()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.copyButton.setOnClickListener {
            copyTextToClipboard()
        }

        binding.copyText.setOnClickListener {
            copyTextToClipboard()
        }

        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setDataContents() {
        binding.copyText.text = shareLink
    }

    private fun copyTextToClipboard() {
        Toast.makeText(requireContext(), R.string.url_copied, Toast.LENGTH_SHORT).show()
        val clipboardManager = context?.let { ContextCompat.getSystemService(it, ClipboardManager::class.java) }
        val clip = ClipData.newPlainText("Label", shareLink)
        clipboardManager?.setPrimaryClip(clip)
        this.dismiss()
    }
}