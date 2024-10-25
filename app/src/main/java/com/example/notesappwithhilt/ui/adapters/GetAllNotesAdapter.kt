package com.example.notesappwithhilt.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.ViewCompat
import com.example.notesappwithhilt.databinding.NotesEachItemLayoutBinding
import com.example.notesappwithhilt.models.Note

class GetAllNotesAdapter(
    private var notesList: List<Note>,
    private var noteColors: List<String>,
    val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<GetAllNotesAdapter.ViewHolder>() {

    fun setNewList(newList: List<Note>, newNoteColors: List<String>) {
        this.notesList = newList
        this.noteColors = newNoteColors
        notifyDataSetChanged()  // You can use more granular notify methods like notifyItemRangeChanged() for better performance.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotesEachItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        val noteColor = noteColors[position] // Fallback to white if color list is shorter
        holder.bind(note, position, noteColor)
    }

    override fun getItemCount(): Int = notesList.size

    inner class ViewHolder(private val binding: NotesEachItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, position: Int, color: String) {
            binding.noteTitleTv.text = note.title

            // Show bookmark view based on isBookmarked status
            binding.bookmarkView.visibility = if (note.isBookmarked == true) View.VISIBLE else View.GONE

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
