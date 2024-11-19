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
import com.example.notesappwithhilt.databinding.FragmentRegisterBinding
import com.example.notesappwithhilt.models.SignUpResponse
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    var responseData : SignUpResponse? = null

    @Inject
    lateinit var prefManager: PrefManager

    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater , container , false)

        listener()
        observer()

        return binding.root
}

    private fun observer() {
        authViewModel.registerUser.observe(viewLifecycleOwner) {
            when(it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    responseData = it.value // Assign the response data here
                    if (responseData?.statusCode == KeyConstants.SUCCESSCODE) {
                        binding.signInWarning.visibility = View.GONE
                        App.app.prefManager.isLoggedIn = true
                        App.app.prefManager.accessToken = responseData?.token
                        App.app.prefManager.logginUserData = responseData?.data
                        Log.d("prefmanager", responseData?.token ?: "")
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                    } else {
                         Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                    binding.signInWarning.visibility = View.VISIBLE
                }
                null -> {}
            }
        }
    }


    private fun listener() {
        binding.gotoSignInPage.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            val name = binding.enterName.text.toString()
            val email = binding.enterEmail.text.toString()
            val password = binding.enterPassword.text.toString()

            // Validate name, email, and password
            if (name.isEmpty()) {
                showToast("Name is required")
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                showToast("Email is required")
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                showToast("Invalid email format")
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                showToast("Password is required")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showToast("Password must be at least 6 characters")
                return@setOnClickListener
            }

            // If validation passes, proceed with registration
            val params = HashMap<String, String>()
            params["name"] = name
            params["email"] = email
            params["password"] = password
            authViewModel.registerUser(params)
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