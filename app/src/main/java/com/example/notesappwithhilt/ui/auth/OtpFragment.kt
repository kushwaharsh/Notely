package com.example.notesappwithhilt.ui.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.PrefManager
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.FragmentOtpBinding
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class OtpFragment : Fragment() {

    private lateinit var binding : FragmentOtpBinding
    private val authViewModel : AuthViewModel by viewModels()
    private var countDownTimer: CountDownTimer? = null

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater , container , false)

        intiview()
        observer()
        listener()
        startCountdown()

        return binding.root
    }

    private fun observer() {
        authViewModel.verifyOtp.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {

                        when (it.value.isExists){
                            true -> {
                                prefManager.isLoggedIn = true
                                prefManager.accessToken = it.value.token
                                prefManager.logginUserData = it.value.data
                                Log.d("prefmanagerOtp", prefManager.accessToken.toString())

                                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_otpFragment_to_homeFragment)
                            }
                            false -> {
                                val email = arguments?.getString("user_email")
                                val bundle = Bundle().apply {
                                    putString("user_email", email)
                                }
                                findNavController().navigate(R.id.action_otpFragment_to_registerFragment , bundle)
                            }
                            null -> {

                            }
                        }

                    } else {
                        ProgressBarUtils.hideProgressDialog()
                        Toast.makeText(requireContext(), "OTP Verification Failed", Toast.LENGTH_SHORT).show()

                    }
                }
                is Resource.Faliure -> { // Fixed typo: 'Faliure' to 'Failure'
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "OTP Verification Failed", Toast.LENGTH_SHORT).show()

                }
                null -> {}
            }
        }
    }


    private fun listener() {
        binding.mobileOtpPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) {
                    val enteredOtp = s.toString()
                    val email = arguments?.getString("user_email")
                    val params = HashMap<String, String>()
                    params["email"] = email.toString()
                    params["otp"] = enteredOtp
                    authViewModel.verifyOtp(params)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.resendOtpBtn.setOnClickListener {
            val params = HashMap<String, String>()
            val email = arguments?.getString("user_email")
            params["email"] = email.toString()
            authViewModel.sendOtp(params)
            binding.mobileOtpPinView.setText("")
            startCountdown()
        }

        binding.backBtn.setOnClickListener {
            val navController = findNavController()
            val currentDestination = navController.currentDestination?.id
            Log.d("NavigationDebug", "Current destination: $currentDestination")

            if (currentDestination == R.id.otpFragment) {
                Log.d("NavigationDebug", "Navigating to LoginFragment.")
                navController.navigate(R.id.action_otpFragment_to_loginFragment)
            } else if (!navController.popBackStack()) {
                Log.d("NavigationDebug", "No fragments in the back stack. Exiting.")
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }



    }



    private fun intiview() {
        val email = arguments?.getString("user_email")
       binding.emailTv.text = email
    }

    private fun startCountdown() {

        binding.resendLayout.visibility = View.GONE
        binding.timerLayout.visibility = View.VISIBLE

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                binding.countDownTimerTv.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.resendLayout.visibility = View.VISIBLE
                binding.timerLayout.visibility = View.GONE


            }
        }.start()
    }

}