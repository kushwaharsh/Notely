package com.example.notesappwithhilt.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.databinding.TagEachItemLayoutBinding
import com.example.notesappwithhilt.models.Note
import com.example.notesappwithhilt.models.TagList

class NoteTagAdapter(
    private var tagsList: List<TagList>,
    private val onTagSelected: (String) -> Unit,
) : RecyclerView.Adapter<NoteTagAdapter.TagViewHolder>() {

    private var selectedTag: String? = "All" // Change to a single selected tag

    fun setNewList(tagList: ArrayList<TagList>) {
        this.tagsList = tagList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding =
            TagEachItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(tagsList[position])
    }

    override fun getItemCount(): Int = tagsList.size

    inner class TagViewHolder(private val binding: TagEachItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TagList) {
            binding.tagName.text = data.tag

            // Check if the tag is selected
            if (data.tag == selectedTag) {
                // Tag is selected
                binding.root.setBackgroundResource(R.drawable.rounded_border_red) // Selected background
               // binding.root.setBackgroundResource(R.drawable.gradient_red_style) // Selected background
                binding.tagName.setTextColor(Color.WHITE) // Selected text color
            } else {
                // Tag is unselected
                binding.root.setBackgroundResource(R.drawable.rounded_border_grey) // Unselected background
                binding.tagName.setTextColor(Color.GRAY) // Unselected text color
            }

            // Handle tag click
            binding.root.setOnClickListener {
                // If the clicked tag is already selected, deselect it
                if (data.tag == selectedTag) {
                    selectedTag = null // Deselect
                } else {
                    selectedTag = data.tag // Select the new tag
                }
                onTagSelected(selectedTag ?: "") // Notify about the selected tag
                notifyDataSetChanged() // Refresh the view to update the backgrounds
            }
        }
    }
}

