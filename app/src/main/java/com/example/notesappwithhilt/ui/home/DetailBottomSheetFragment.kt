package com.example.notesappwithhilt.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesappwithhilt.databinding.FragmentDetailBottomSheetBinding
import com.example.notesappwithhilt.models.NoteById
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject


class DetailBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDetailBottomSheetBinding
    private var jsonData: String? = null
    private var noteDetails: NoteById? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailBottomSheetBinding.inflate(inflater, container, false)
        initview()
        return binding.root
    }

    private fun initview() {

        arguments?.let {
            jsonData = it.getString("NoteDetails")
            noteDetails = Gson().fromJson(jsonData, NoteById::class.java)
        }

        binding.detailsBtn.setOnClickListener {
            val bottomsheet = NoteDetailsBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString("NoteDetails", jsonData ?: "")
                }
            }
            bottomsheet.show(parentFragmentManager, "NoteDetailsBottomSheet")
        }

        binding.shareBtn.setOnClickListener {
            val noteString = noteDetails?.let {
                // Format the note details into a readable string for sharing
                """
        Install the Notely App and collaborate with this note: NOTELY
        
        Title: ${it.title}
        Content: ${it.content}
        """.trimIndent() // Adds a newline before "Title"
            } ?: "No details available"

            // Prepare the sharing intent
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, noteString)  // Pass the formatted note details
            }

            // Start the activity to share the data
            startActivity(Intent.createChooser(shareIntent, "Share Note via"))
        }


    }

}