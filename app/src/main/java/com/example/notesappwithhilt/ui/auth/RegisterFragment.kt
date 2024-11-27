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

    @Inject
    lateinit var prefManager: PrefManager

    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater , container , false)

        initview()
        listener()
        observer()

        return binding.root
}

    private fun initview() {
        binding.enterEmail.setText(arguments?.getString("user_email"))
    }

    private fun observer() {
        authViewModel.registerUser.observe(viewLifecycleOwner) {
            when(it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESSCODE) {
                        prefManager.isLoggedIn = true
                        prefManager.accessToken = it.value.token
                        prefManager.logginUserData = it.value.data
                        Log.d("appToken", prefManager.accessToken ?: "")
                        Log.d("userId", prefManager.logginUserData?._id ?: "")

                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                    } else {
                         Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                }
                null -> {}
            }
        }
    }


    private fun listener() {

        binding.registerBtn.setOnClickListener {
            val name = binding.enterName.text.toString()

            // Validate name, email, and password
            if (name.isEmpty()) {
                showToast("Name is required")
                return@setOnClickListener
            }

            // If validation passes, proceed with registration
            val params = HashMap<String, String>()
            val email_user = arguments?.getString("user_email")
            params["name"] = name
            params["email"] = email_user.toString()
            authViewModel.registerUser(params)
        }
    }

    // Function to show toast messages
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}