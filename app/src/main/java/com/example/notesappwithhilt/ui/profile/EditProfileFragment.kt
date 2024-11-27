package com.example.notesappwithhilt.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.DialogLayoutBinding
import com.example.notesappwithhilt.databinding.FragmentEditProfileBinding
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        initview()
        listener()
        observer()

        return binding.root
    }

    private fun observer() {
        authViewModel.deleteAccount.observe(viewLifecycleOwner){
            when(it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }
                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    App.app.prefManager.clearPreferences()
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)  // Pop all fragments up to the home fragment
                        .setLaunchSingleTop(true)  // Launch the new fragment without adding it to the back stack
                        .build()

                    findNavController().navigate(R.id.registerFragment, null, navOptions)
                    Toast.makeText(requireContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                }
                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
                null -> {}
            }
        }
    }


    private fun listener() {
        binding.editAndSaveBtn.setOnClickListener {
            if (binding.editAndSaveBtn.text.equals("Edit Profile")) {
                binding.editAndSaveBtn.text = "Save Profile"
                editTextFields(true)
            } else {
                binding.editAndSaveBtn.text = "Edit Profile"
                editTextFields(false)
            }
        }

        binding.deleteAccountBtn.setOnClickListener {
            val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
            val layoutDialog = BlurDialog(requireActivity(), R.style.TransparentDialogTheme)
            layoutDialog.setContentView(dialogBinding.root)

            dialogBinding.dialogTitle.text = "Delete Account ?"
            dialogBinding.dialogSubTitle.text = "Are you sure you wanna delete your account (All your data will be removed from our database and can`t be recovered) ?"



            dialogBinding.btnCancel.setOnClickListener {
                layoutDialog.dismiss()
            }
            dialogBinding.btnConfirm.setOnClickListener {
                authViewModel.deleteAccount(App.app.prefManager.accessToken.toString() , App.app.prefManager.logginUserData?._id.toString())
                layoutDialog.dismiss()
            }

            layoutDialog.show()
        }
    }

    private fun editTextFields(isEditable: Boolean) {
        binding.fullnameTv.isEnabled = isEditable
        binding.recoveryPhoneTv.isEnabled = isEditable

        val textColor = if (isEditable) Color.GRAY else Color.WHITE
        binding.emailTv.setTextColor(textColor)
        binding.subscriptionStatusTv.setTextColor(textColor)
        binding.collaborationMemberTv.setTextColor(textColor)
    }

    private fun initview() {
        binding.fullnameTv.setText(App.app.prefManager.logginUserData?.name ?: "")
        binding.emailTv.setText(App.app.prefManager.logginUserData?.email ?: "")
        binding.recoveryPhoneTv  .setText("No Recover No Found")
        binding.subscriptionStatusTv.setText("Subscription Inactive")
        binding.collaborationMemberTv.setText("No Collaborations Yet")
    }
}