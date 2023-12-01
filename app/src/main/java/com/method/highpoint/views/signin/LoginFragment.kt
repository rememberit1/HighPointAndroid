package com.method.highpoint.views.signin

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.method.highpoint.MainActivityViewModel
import com.method.highpoint.R
import com.method.highpoint.R.color
import com.method.highpoint.databinding.FragmentLoginBinding
import com.method.highpoint.utils.isOnline
import com.method.highpoint.utils.showConnectionAlert
import com.method.highpoint.views.MyMarketFragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.passwordText.addTextChangedListener {
            checkToEnableButton()
        }

        binding.reset.setOnClickListener {
            replaceFragment(ForgotPasswordFragment())
        }

        binding.registerHere.setOnClickListener {
            replaceFragment(RegistrationFragment())
        }

        binding.buttonSignIn.setOnClickListener {
            checkLogin()
        }
    }

    private fun checkToEnableButton() {
        if (!binding.emailAddressText.text.isNullOrBlank() && !binding.passwordText.text.isNullOrBlank()) {
            binding.buttonSignIn.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_corners)
            binding.buttonSignIn.setTextColor(resources.getColor(color.white))
            binding.buttonSignIn.isEnabled = true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
        fragmentTransaction?.commit()
    }

    private fun checkLogin() {
        if (isOnline(requireContext())) {
            viewModel.signIn(
                binding.emailAddressText.text.toString(),
                binding.passwordText.text.toString()
            )

            if (viewModel.userInfo?.stat == "ok") {
                replaceFragment(MyMarketFragment("default"))
                val userGuid = viewModel.userInfo?.capture_user?.uuid
                val sharedPreference = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with(sharedPreference.edit()) {
                    this?.putString("UserGUID", userGuid) ?: ""
                    this?.apply()
                }
            } else {
                val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.HPDialogTheme)
                    .setTitle(resources.getString(R.string.signin_error_title))
                    .setMessage(resources.getString(R.string.signin_error_description))
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(color.hp_primary))
            }
        } else {
            showConnectionAlert(requireContext())
        }
    }
}