package com.method.highpoint.views.signin

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.LinkMovementMethod
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
import com.method.highpoint.databinding.FragmentRegistrationBinding
import com.method.highpoint.utils.isOnline
import com.method.highpoint.utils.showConnectionAlert
import com.method.highpoint.views.MyMarketFragment

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setupViews()
        binding.backArrow.setOnClickListener {
            replaceFragment(LoginFragment())
        }
    }

    private fun setupViews() {

        binding.createAccountDescription.movementMethod = LinkMovementMethod.getInstance()

        binding.emailAddressText.doAfterTextChanged {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailAddressText.text.toString()).matches())
                binding.invalidEmail.visibility = View.VISIBLE

            else
                binding.invalidEmail.visibility = View.INVISIBLE
            checkButtonConditions()
        }

        binding.confirmPasswordText.doAfterTextChanged {
            if (!binding.passwordText.text.toString().equals(binding.confirmPasswordText.text.toString()))
                binding.passwordNonMatching.visibility = View.VISIBLE
            else
                binding.passwordNonMatching.visibility = View.INVISIBLE
            checkButtonConditions()
        }

        binding.switchButton.setOnCheckedChangeListener { _, _ ->
            checkButtonConditions()
        }

        binding.buttonCreateAccount.setOnClickListener {
            checkRegistration()
        }
    }

    private fun enableButton() {
        binding.buttonCreateAccount.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_corners)
        binding.buttonCreateAccount.setTextColor(resources.getColor(R.color.white))
        binding.buttonCreateAccount.isEnabled = true
    }

    private fun disableButton() {
        binding.buttonCreateAccount.isEnabled = false
        binding.buttonCreateAccount.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_disabled_corners)
        binding.buttonCreateAccount.setTextColor(resources.getColor(R.color.text_color_button))
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
        fragmentTransaction?.commit()
    }

    private fun checkRegistration() {
        if (isOnline(requireContext())) {
            viewModel.registerUser(
                emailAddress = binding.emailAddressText.text.toString(),
                newPassword = binding.passwordText.text.toString(),
                lastName = binding.lastNameText.text.toString(),
                firstName = binding.firstNameText.text.toString(),
            )

            if (viewModel.userInfo?.stat == "ok") {
                val userGuid = viewModel.userInfo?.capture_user?.uuid
                val sharedPreference = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with(sharedPreference.edit()) {
                    this?.putString("UserGUID", userGuid) ?: ""
                    this?.apply()
                }
                replaceFragment(MyMarketFragment("default"))
            } else {
                val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.HPDialogTheme)
                    .setTitle(resources.getString(R.string.registration_error_title))
                    .setMessage(resources.getString(R.string.registration_error_description))
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(
                    R.color.hp_primary))
            }
        } else {
            showConnectionAlert(requireContext())
        }
    }

    private fun checkButtonConditions() {
        if (
            !binding.emailAddressText.text.isNullOrBlank() && !binding.switchButton.isChecked &&
            Patterns.EMAIL_ADDRESS.matcher(binding.emailAddressText.text.toString()).matches() &&
            !binding.passwordText.text.isNullOrBlank() && !binding.confirmPasswordText.text.isNullOrBlank()
            && !binding.firstNameText.text.isNullOrBlank() && !binding.lastNameText.text.isNullOrBlank()
            && binding.passwordText.text.toString() == binding.confirmPasswordText.text.toString()
        ) {
            enableButton()
        } else {
            disableButton()
        }
    }

}