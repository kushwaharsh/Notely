package com.example.notesappwithhilt.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.databinding.DialogLayoutBinding
import com.example.notesappwithhilt.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater , container , false)

        initview()
        listener()
        return binding.root
    }

    private fun initview() {
        
        binding.userNameTv.text = App.app.prefManager.logginUserData?.name
        binding.emailTv.text = App.app.prefManager.logginUserData?.email
    }

    private fun listener() {

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buyPremiumTv.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_premiumFragment)
        }

        binding.editProfileTv.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.notificationsTv.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_notificationFragment)
        }

        binding.logoutTv.setOnClickListener {
            val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
            val layoutDialog = BlurDialog(requireActivity(), R.style.TransparentDialogTheme)
            layoutDialog.setContentView(dialogBinding.root)

            dialogBinding.dialogTitle.text = "Log out ?"
            dialogBinding.dialogSubTitle.text = "Are you sure you wanna Logout ?"



            dialogBinding.btnCancel.setOnClickListener {
                layoutDialog.dismiss()
            }
            dialogBinding.btnConfirm.setOnClickListener {
                App.app.prefManager.clearPreferences()
                layoutDialog.dismiss()

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, true)  // Pop all fragments up to the home fragment
                    .setLaunchSingleTop(true)  // Launch the new fragment without adding it to the back stack
                    .build()

                findNavController().navigate(R.id.loginFragment, null, navOptions)
            }

            layoutDialog.show()
        }

        binding.linkedinBtn.setOnClickListener {
            openSocialMediaApp("https://www.linkedin.com/in/harsh-raj-kushwaha-2000111ab/")
        }
        binding.githubBtn.setOnClickListener {
            openSocialMediaApp("https://github.com/kushwaharsh")
        }
        binding.instaBtn.setOnClickListener {
            openSocialMediaApp("https://www.linkedin.com/in/harsh-raj-kushwaha-2000111ab/")
        }
        binding.twitterBtn.setOnClickListener {
            openSocialMediaApp("https://x.com/kushwaha_rsh?t=5fLELUYp8-P22CcfTiZo_w&s=09")
        }
    }

    private fun openSocialMediaApp(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        } ?: run {
            Toast.makeText(requireContext(), "URL is not available", Toast.LENGTH_SHORT).show()
        }
    }

}