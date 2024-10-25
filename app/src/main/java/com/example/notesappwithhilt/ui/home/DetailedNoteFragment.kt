package com.example.notesappwithhilt.ui.home

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.KeyConstants.TAG
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.DialogLayoutBinding
import com.example.notesappwithhilt.databinding.FragmentDetailedNoteBinding
import com.example.notesappwithhilt.models.NoteById
import com.example.notesappwithhilt.viewModels.AuthViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailedNoteFragment : Fragment() {

    private lateinit var binding: FragmentDetailedNoteBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var noteDetails : NoteById? = null
    private var receivedNoteId = ""
    private var receivedNoteColor = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedNoteBinding.inflate(layoutInflater, container, false)
        receivedNoteId = arguments?.getString("ClickedNoteId") ?: ""
        receivedNoteColor = arguments?.getString("ClickedNoteColor") ?: ""
        initview()
        observer()
        listener()
        return binding.root
    }

    private fun observer() {
        authViewModel.getNoteById.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {

                        noteDetails = it.value.note

                        binding.noteTitleTv.text = it.value.note?.title.toString()


                        if (it.value.note?.content?.isEmpty() == true){
                             binding.noteContentTv.visibility = View.GONE
                            binding.noteBoardTv.visibility = View.VISIBLE

                            val decodedBitmap = it.value.note.whiteboard?.let { decodeBase64ToBitmap(it) }
                            if (decodedBitmap != null) {
                                binding.noteBoardTv.setImageBitmap(decodedBitmap)
                            }

                        }else{
                            binding.noteContentTv.visibility = View.VISIBLE
                            binding.noteBoardTv.visibility = View.GONE
                            binding.noteContentTv.text = it.value.note?.content.toString()
                        }

                        if (it.value.note?.isBookmarked == true){
                            binding.bookmarkImage.visibility = View.VISIBLE
                        }else{
                            binding.bookmarkImage.visibility = View.GONE
                        }
                        binding.selectedLabel.text = "Label : ${it.value.note?.tag}"


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

        authViewModel.deleteNote.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        Toast.makeText(requireContext(), "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_detailedNoteFragment_to_homeFragment)
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

        binding.deleteBtn.setOnClickListener {
            val dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
            val layoutDialog = BlurDialog(requireActivity(), R.style.TransparentDialogTheme)
            layoutDialog.setContentView(dialogBinding.root)

            dialogBinding.dialogTitle.text = "Delete Note"
            dialogBinding.dialogSubTitle.text = "Are you sure you want to delete this note ?"

            dialogBinding.crossBtn.setOnClickListener {
                layoutDialog.dismiss()
            }

            dialogBinding.btnCancel.setOnClickListener {
                layoutDialog.dismiss()
            }
            dialogBinding.btnConfirm.setOnClickListener {
                authViewModel.deleteNote(
                    App.app.prefManager.accessToken.toString(),
                    receivedNoteId.toString()
                )
                layoutDialog.dismiss()
            }

            layoutDialog.show()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("ClickedNoteId", receivedNoteId)
            }
            findNavController().navigate(R.id.action_detailedNoteFragment_to_editNoteFragment , bundle)
        }

        binding.moreOptionsBtn.setOnClickListener {
            var jsonData = Gson().toJson(noteDetails)
            val bottomsheet = DetailBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString("NoteDetails", jsonData)
                }
            }
            bottomsheet.show(parentFragmentManager, "DetailedBottomSheet")
        }
    }

    private fun initview() {
        Log.d(TAG, receivedNoteId.toString())

        // Set the background color based on the received color
        if (receivedNoteColor.isNotEmpty()) {
            // Assuming the color is in hex format like "ffcccc", prepend "#" and set it
            binding.noteBorder.backgroundTintList =  ColorStateList.valueOf(android.graphics.Color.parseColor("#$receivedNoteColor"))
        }

        authViewModel.getNoteById(
            App.app.prefManager.accessToken.toString(),
            receivedNoteId.toString()
        )

    }

    // Helper function to decode Base64 string to Bitmap
    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        if (base64Str.isEmpty()) {
            Log.e("Base64Decode", "Base64 string is empty")
            return null
        }

        Log.d("Base64Decode", "Base64 String Length: ${base64Str.length}")
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.NO_WRAP)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            if (bitmap != null) {
                if (bitmap.width > 0 && bitmap.height > 0) {
                    bitmap
                } else {
                    Log.e("BitmapDecode", "Decoded bitmap has invalid dimensions: ${bitmap.width}x${bitmap.height}")
                    null
                }
            } else {
                Log.e("BitmapDecode", "BitmapFactory returned null")
                null
            }
        } catch (e: IllegalArgumentException) {
            Log.e("Base64Decode", "IllegalArgumentException: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("Base64Decode", "Exception: ${e.message}")
            null
        }
    }

}