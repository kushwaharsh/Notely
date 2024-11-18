package com.example.notesappwithhilt.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappwithhilt.databinding.NotesEachItemLayoutBinding
import com.example.notesappwithhilt.models.Note
import com.google.android.material.snackbar.Snackbar

class GetAllNotesAdapter(
    private var notesList: List<Note>,
    private var noteColors: List<String>,
    val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<GetAllNotesAdapter.ViewHolder>() {

    private var filteredNotesList = notesList // New list to store filtered notes
    private val handler = Handler(Looper.getMainLooper())
    private var typingFinishedRunnable: Runnable? = null

    fun setNewList(newList: List<Note>, newNoteColors: List<String>) {
        this.notesList = newList
        this.noteColors = newNoteColors
        this.filteredNotesList = newList // Update filtered list as well
        notifyDataSetChanged()
    }

    // Function to filter notes based on search query
    // Function to filter notes based on search query and show Snackbar if no matches are found
    fun filter(query: String, parentView: View) {
        filteredNotesList = if (query.isEmpty()) {
            notesList
        } else {
            notesList.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
            }
        }

        notifyDataSetChanged()

        // Cancel any existing delayed toast if user continues typing
        typingFinishedRunnable?.let { handler.removeCallbacks(it) }

        // Create a new runnable to display the toast with delay
        typingFinishedRunnable = Runnable {
            if (filteredNotesList.isEmpty()) {
                Snackbar.make(parentView, "No Note Found", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Post the toast display with delay (e.g., 500ms)
        handler.postDelayed(typingFinishedRunnable!!, 500)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotesEachItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = filteredNotesList[position]
        val noteColor = if (position < noteColors.size) noteColors[position] else "FFFFFF" // Fallback to white if color list is shorter
        holder.bind(note, position, noteColor)
    }

    override fun getItemCount(): Int = filteredNotesList.size

    inner class ViewHolder(private val binding: NotesEachItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, position: Int, color: String) {
            binding.noteTitleTv.text = note.title

            // Show bookmark view based on isBookmarked status
            binding.bookmarkView.visibility = if (note.isBookmarked == true) View.VISIBLE else View.GONE

            if (note.tag == "Finished"){
                binding.finishedStamp.visibility = View.VISIBLE
            }else{
                binding.finishedStamp.visibility = View.GONE
            }

            // Set note content or whiteboard based on the note's content
            if (note.content.isEmpty()) {
                binding.noteContentTv.visibility = View.GONE
                binding.noteBoardTv.visibility = View.VISIBLE

                val decodedBitmap = decodeBase64ToBitmap(note.whiteboard)
                if (decodedBitmap != null) {
                    binding.noteBoardTv.setImageBitmap(decodedBitmap)
                } else {
                    binding.noteBoardTv.setImageBitmap(null) // Clear if decoding fails
                }

            } else {
                binding.noteContentTv.visibility = View.VISIBLE
                binding.noteBoardTv.visibility = View.GONE
                binding.noteContentTv.text = note.content
            }

            // Set background tint color with proper error handling for invalid colors
            try {
                ViewCompat.setBackgroundTintList(binding.root, ColorStateList.valueOf(Color.parseColor("#$color")))
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                ViewCompat.setBackgroundTintList(binding.root, ColorStateList.valueOf(Color.parseColor("#FFFFFF")))  // Default to white on error
            }

            itemView.setOnClickListener {
                clickListener.invoke(position)
            }
        }
    }

    // Helper function to decode Base64 string to Bitmap
    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)?.let {
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
