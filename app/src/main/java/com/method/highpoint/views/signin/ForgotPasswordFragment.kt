package com.method.highpoint.views.signin

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.databinding.FragmentForgotPasswordBinding
import com.method.highpoint.utils.isOnline
import com.method.highpoint.utils.showConnectionAlert

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.backArrow.setOnClickListener {
            replaceFragment(LoginFragment())
        }

        binding.emailAddressText.doAfterTextChanged {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.emailAddressText.text.toString()).matches())
                enableButton()
            else
                disableButton()
        }

        binding.resetPassword.setOnClickListener {
            checkForgotPassword()
        }
    }

    private fun enableButton() {
        binding.resetPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_corners)
        binding.resetPassword.setTextColor(resources.getColor(R.color.white))
        binding.resetPassword.isEnabled = true
    }

    private fun disableButton() {
        binding.resetPassword.isEnabled = false
        binding.resetPassword.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_disabled_corners)
        binding.resetPassword.setTextColor(resources.getColor(R.color.text_color_button))
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
        fragmentTransaction?.commit()
    }

    private fun checkForgotPassword() {
        if (isOnline(requireContext())) {
            viewModel.forgotPassword(
                emailAddress = binding.emailAddressText.text.toString()
            )
            if (viewModel.forgotPassword?.stat == "ok") {
                binding.content.visibility = View.GONE
                binding.success.visibility = View.VISIBLE
            } else {
                val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.HPDialogTheme)
                    .setTitle(R.string.forgot_password_problem)
                    .setMessage(R.string.forgot_password_problem_description)
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(
                    R.color.hp_primary))
            }
        } else {
            showConnectionAlert(requireContext())
        }
    }
}