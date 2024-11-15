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
        authViewModel.signInUser.observe(viewLifecycleOwner) { resource ->
            when (resource) {

                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    // Check if response is successful
                    if (resource.value?.statusCode == KeyConstants.SUCCESS) {
                        binding.registerWarning.visibility = View.GONE
                        App.app.prefManager.isLoggedIn = true // Changed to true on successful login

                        // Set the response data correctly to ensure it's not null
                        responseData = resource.value // <-- Store the response data here

                        // Ensure we only save token and user data if they are not null
                        responseData?.let { data ->
                            Log.d("LoginToken", data.token.toString())
                            App.app.prefManager.accessToken = data.token // Save access token
                            App.app.prefManager.logginUserData = data.data // Save user data
                        }

                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        ProgressBarUtils.hideProgressDialog()
                        binding.registerWarning.visibility = View.VISIBLE
                    }
                }
                is Resource.Faliure -> { // Fixed typo: 'Faliure' to 'Failure'
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
                null -> {}
            }
        }
    }

    private fun listener() {
        binding.loginBtn.setOnClickListener {
            val params = HashMap<String, String>()
            params["email"] = binding.enterEmail.text.toString()
            params["password"] = binding.enterPassword.text.toString()
            authViewModel.signInUser(params)
        }

        binding.gotoRegisterationPage.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
