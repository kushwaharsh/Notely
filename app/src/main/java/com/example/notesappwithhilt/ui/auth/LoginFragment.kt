package com.example.notesappwithhilt.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.PrefManager
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.FragmentLoginBinding
import com.example.notesappwithhilt.models.SignUpResponse
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    var responseData: SignUpResponse? = null

    @Inject
    lateinit var prefManager: PrefManager
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        listener()
        observer()

        return binding.root
    }

    private fun observer() {
        authViewModel.sendOtp.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    // Check if response is successful
                    if (resource.value?.statusCode == KeyConstants.SUCCESS) {
                        val email = resource.value.email
                        val bundle = Bundle().apply {
                            putString("user_email", email)
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_otpFragment , bundle)
                        Toast.makeText(requireContext(), "OTP sent Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        ProgressBarUtils.hideProgressDialog()
                         Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Faliure -> { // Fixed typo: 'Faliure' to 'Failure'
                    ProgressBarUtils.hideProgressDialog()
                }
                null -> {}
            }
        }
    }

    private fun listener() {
        binding.loginBtn.setOnClickListener {
            val email = binding.enterEmail.text.toString()

            // Validate email and password
            if (email.isEmpty()) {
                showToast("Email is required")
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                showToast("Invalid email format")
                return@setOnClickListener
            }
            // If validation passes, proceed with sign-in
            val params = HashMap<String, String>()
            params["email"] = email
            authViewModel.sendOtp(params)
        }

    }

    // Utility function to check valid email format
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to show toast messages
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
