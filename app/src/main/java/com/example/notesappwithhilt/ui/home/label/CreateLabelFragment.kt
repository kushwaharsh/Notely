package com.example.notesappwithhilt.ui.home.label

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.FragmentCreateLabelBinding
import com.example.notesappwithhilt.ui.home.CreateNoteFragment
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateLabelFragment(val layoutDialog: BlurDialog, var labelName: (String) -> Unit) :
    Fragment() {

    private lateinit var binding: FragmentCreateLabelBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCreateLabelBinding.inflate(inflater, container, false)

        initView()
        listener()
        observer()
        return binding.root
    }

    private fun observer() {
        authViewModel.createTag.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        Toast.makeText(
                            requireContext(),
                            "Label created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
//                        CreateNoteFragment.createdTag = it.value.lable?.tag ?: ""
                        labelName(it.value.label?.tag ?: "")
                    }
                }

                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                        .show()
                }

                null -> {}
            }
        }
    }


    private fun listener() {
        binding.btnCancel.setOnClickListener {
            layoutDialog.dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            val newLabel = binding.createTagTv.text.toString()
            val params = HashMap<String, String>()
            params["tag"] = newLabel
            params["userId"] = App.app.prefManager.logginUserData?._id.toString()
            authViewModel.createTag(params)
            layoutDialog.dismiss()
        }
    }

    private fun initView() {
    }


}