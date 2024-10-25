package com.example.notesappwithhilt.ui.home

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
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.FragmentEditNoteBinding
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var receivedNoteId = ""
    private var isBookmarked: Boolean = false
    private var whiteboardBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditNoteBinding.inflate(layoutInflater, container, false)
        receivedNoteId = arguments?.getString("ClickedNoteId") ?: ""
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
                        binding.noteTitleTv.setText(it.value.note?.title)

                        // Check if content or whiteboard exists
                        val noteContent = it.value.note?.content ?: ""
                        val whiteboardBase64 = it.value.note?.whiteboard

                        if (noteContent.isEmpty() && !whiteboardBase64.isNullOrEmpty()) {
                            // Show whiteboard if content is empty
                            binding.noteContentTv.visibility = View.GONE
                            binding.whiteBoardView.visibility = View.VISIBLE

                            whiteboardBitmap = decodeBase64ToBitmap(whiteboardBase64)
                            whiteboardBitmap?.let { bitmap ->
                                binding.whiteBoardView.setWhiteBoardBitmap(bitmap)
                            }
                        } else {
                            // Show text content if available
                            binding.noteContentTv.visibility = View.VISIBLE
                            binding.whiteBoardView.visibility = View.GONE
                            binding.noteContentTv.setText(noteContent)
                        }
                    }
                }

                is Resource.Faliure -> {
                    ProgressBarUtils.hideProgressDialog()
                    Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }

                null -> {}
            }
        }

        authViewModel.updateNote.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        Toast.makeText(requireContext(), "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
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
        binding.bookmarkFrame.setOnClickListener {
            isBookmarked = !isBookmarked
            binding.bookmarkView.visibility = if (isBookmarked) View.GONE else View.VISIBLE
            binding.bookmarkViewTinted.visibility = if (isBookmarked) View.VISIBLE else View.GONE
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            saveNote()
        }
    }

    private fun initview() {
        authViewModel.getNoteById(App.app.prefManager.accessToken.toString(), receivedNoteId)
    }

    private fun saveNote() {
        val title = binding.noteTitleTv.text.toString()
        val content = binding.noteContentTv.text.toString()

        // Validate that the title is not empty
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the Base64 encoded string from the whiteboard if it's visible
        val whiteboardBase64 = if (binding.whiteBoardView.visibility == View.VISIBLE) {
            val bitmap = binding.whiteBoardView.getDrawingBitmap()
            bitmap?.let { convertBitmapToBase64(it) }
        } else {
            null
        }
        if (whiteboardBase64 != null) {
            Log.d("bitmapValue", whiteboardBase64)
        }

        // Ensure that at least one of the content fields is filled
        if (content.isEmpty() && whiteboardBase64 == null) {
            Toast.makeText(requireContext(), "Please fill either the content or the whiteboard.", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare parameters for the API request
        val params = HashMap<String, String>().apply {
            put("title", title)
            put("content", content)
            if (whiteboardBase64 != null) {
                put("whiteboard", whiteboardBase64)  // Add Base64 content if available
            }
            // put("isBookmarked", isBookmarked.toString())
            // put("tag", createdTag ?: "All")
        }

        // Call ViewModel method to create the note
        authViewModel.updateNote(App.app.prefManager.accessToken.toString(), receivedNoteId, params)
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()

        // Attempt to compress the bitmap and handle possible failure
        val isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Use PNG for better quality

        if (!isSuccess) {
            Log.e("BitmapEncoding", "Bitmap compression failed")
            return ""
        }

        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }


    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.NO_WRAP) // Ensure to decode without wrap
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.let {
                // Return the bitmap or null if decoding fails
                if (it.width > 0 && it.height > 0) it else null
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
