package com.example.notesappwithhilt.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.commonUtils.ProgressBarUtils
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.databinding.FragmentCreateNoteBinding
import com.example.notesappwithhilt.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import android.util.Base64

@AndroidEntryPoint
class CreateNoteFragment : Fragment() {

    private lateinit var binding: FragmentCreateNoteBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var isBookmarked: Boolean = false
    private var createdTag: String? = "All"
    private var noteDeadline: String? = "None"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateNoteBinding.inflate(layoutInflater, container, false)

        initview()
        obeserver()
        listener()

        return binding.root
    }

    private fun obeserver() {
        authViewModel.createNewNote.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> {
                    ProgressBarUtils.showProgressDialog(requireContext())
                }

                is Resource.Success -> {
                    ProgressBarUtils.hideProgressDialog()
                    if (it.value?.statusCode == KeyConstants.SUCCESS) {
                        Toast.makeText(
                            requireContext(),
                            "Note Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    } else {
                        ProgressBarUtils.hideProgressDialog()
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
        binding.bookmarkFrame.setOnClickListener {
            // Log the current state for debugging
            Log.d("Bookmark", "Bookmark clicked, current state: $isBookmarked")

            // Toggle the bookmark state
            isBookmarked = !isBookmarked

            // Update the views' visibility based on the bookmark state
            if (isBookmarked) {
                binding.bookmarkBtn.visibility = View.GONE
                binding.bookmarkBtnTinted.visibility = View.VISIBLE
                binding.bookmarkImage.visibility = View.VISIBLE
            } else {
                binding.bookmarkBtn.visibility = View.VISIBLE
                binding.bookmarkBtnTinted.visibility = View.GONE
                binding.bookmarkImage.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.clipboardBtn.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment({label ->
                binding.selectedLabel.text = "Tag : ${label}"
                createdTag = label
            }) { deadlineDate ->
                noteDeadline = deadlineDate
                binding.selectedDeadline.visibility = View.VISIBLE
                binding.selectedDeadline.text = "Deadline : ${deadlineDate}"
            }
            bottomSheetFragment.show(parentFragmentManager, "BottomSheetFragment")
        }

        binding.whiteBoardBtn.setOnClickListener {
            binding.whiteBoardView.visibility = View.VISIBLE
            binding.noteContentTv.visibility = View.GONE
            binding.whiteBoardBtn.visibility = View.GONE
            binding.contentBtn.visibility = View.VISIBLE
            binding.clearBoeardBtn.visibility = View.VISIBLE
        }

        binding.contentBtn.setOnClickListener {
            binding.whiteBoardView.visibility = View.GONE
            binding.noteContentTv.visibility = View.VISIBLE
            binding.whiteBoardBtn.visibility = View.VISIBLE
            binding.contentBtn.visibility = View.GONE
            binding.clearBoeardBtn.visibility = View.GONE
        }

        binding.clearBoeardBtn.setOnClickListener {
            binding.whiteBoardView.clearBoard()
        }




    }

    private fun initview() {

        binding.selectedLabel.text = "Label : ${createdTag}"

        binding.saveBtn.setOnClickListener {
            saveNote()
        }

    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
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
            put("isBookmarked", isBookmarked.toString())
            put("tag", createdTag ?: "none")
            put("noteDeadline", noteDeadline ?: "none")
        }

        // Call ViewModel method to create the note
        authViewModel.createNewNote(App.app.prefManager.accessToken ?: "", params)
    }

}