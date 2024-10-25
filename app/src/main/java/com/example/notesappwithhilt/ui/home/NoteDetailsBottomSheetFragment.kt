package com.example.notesappwithhilt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesappwithhilt.databinding.FragmentNoteDetailsBottomSheetBinding
import com.example.notesappwithhilt.models.NoteById
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class NoteDetailsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNoteDetailsBottomSheetBinding
    private var noteDetails: NoteById? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNoteDetailsBottomSheetBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    private fun initview() {

        arguments?.let {
            var jsonData = it.getString("NoteDetails") ?: ""
            noteDetails = Gson().fromJson(jsonData, NoteById::class.java)
        }

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        val date = noteDetails?.dateCreated?.let { inputDateFormat.parse(it) }
        val formattedDate = date?.let { outputDateFormat.format(it) }

        binding.createdDateTv.text = formattedDate ?: "N/A"
    //    binding.createdDateTv.text = noteDetails?.dateCreated

        val updatedDate = noteDetails?.lastModified?.let { inputDateFormat.parse(it) }
        val formattedUpdatedDate = updatedDate?.let { outputDateFormat.format(it) }
        binding.updatedDateTv.text = formattedUpdatedDate ?: "N/A"

      //  binding.updatedDateTv.text = noteDetails?.lastModified
        binding.labelTv.text = noteDetails?.tag

        if (noteDetails?.isBookmarked == true) {
            binding.bookmarkedTv.text = "Bookmarked"
        }else{
            binding.bookmarkedTv.text = "Not Bookmarked"
        }

        if (noteDetails?.noteDeadline == "none") {
            binding.deadlineTv.text = "None"
        }else{
            binding.deadlineTv.text = noteDetails?.noteDeadline
        }

        binding.collabTv.text = "None"
    }

}