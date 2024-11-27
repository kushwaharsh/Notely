package com.example.notesappwithhilt.ui.home.label

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.PrefManager
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.FragmentAddLabelBinding
import com.example.notesappwithhilt.models.Label
import com.example.notesappwithhilt.models.TagList
import com.example.notesappwithhilt.ui.adapters.NoteTagAdapter
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddLabelFragment(val layoutDialog: BlurDialog, var labelName: (String) -> Unit) : Fragment() {

    private lateinit var binding: FragmentAddLabelBinding
    private var labelList = ArrayList<Label>()
    private var labelSelected: String = ""
    private var tagList = ArrayList<TagList>()
    private lateinit var tagAdapter: NoteTagAdapter
    private var selectedLabel: String = "All"

    @Inject
    lateinit var prefManager: PrefManager
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddLabelBinding.inflate(inflater, container, false)

        initView()
        listener()
        observer()

        return binding.root

    }

    private fun observer() {
        authViewModel.getAllTags.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        tagList = it.value.labels as ArrayList<TagList>
                        tagAdapter.setNewList(tagList)
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
            labelName(selectedLabel)
            layoutDialog.dismiss()
        }
    }

    private fun initView() {
        tagAdapter = NoteTagAdapter(tagList) { selectedTag ->
            // Handle tag selection
//            handleTagSelected(selectedTag)
            selectedLabel = selectedTag

        }
        binding.choosetagRv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.choosetagRv.adapter = tagAdapter


    }

    private fun handleTagSelected(selectedTag: String) {
        Log.e("selectedTag", selectedTag)
    }

    override fun onResume() {
        super.onResume()
        authViewModel.getAllTags( prefManager.accessToken.toString() , prefManager.logginUserData?._id.toString())
    }

}