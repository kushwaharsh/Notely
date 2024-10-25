package com.example.notesappwithhilt.ui.home

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.BlurDialog
import com.example.notesappwithhilt.databinding.FragmentBottomSheetBinding
import com.example.notesappwithhilt.databinding.LabelDialogLayoutBinding
import com.example.notesappwithhilt.ui.home.label.LabelPagerAdapter
import com.example.notesappwithhilt.viewModels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class BottomSheetFragment(var label: (String) -> Unit , var deadlineDate: (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var labelPagerAdapter: LabelPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        initview()
        listener()
        return binding.root
    }


    private fun initview() {

    }


    private fun listener() {
        binding.tagBtn.setOnClickListener {
            labelDialogSetup()
            dismiss()
        }

        binding.scheduleBtn.setOnClickListener {
            addEventToCalendar()
        }

        binding.deadlineBtn.setOnClickListener {
            datePickerDialog()
        }
    }

    private fun labelDialogSetup() {
        val dialogBinding = LabelDialogLayoutBinding.inflate(layoutInflater)
        val layoutDialog = BlurDialog(requireActivity(), R.style.TransparentDialogTheme)
        layoutDialog.setContentView(dialogBinding.root)

        dialogBinding.dialogTitle.text = "Add label"
        labelPagerAdapter = LabelPagerAdapter(requireActivity(), layoutDialog) {
            label(it)
        }
        dialogBinding.labelViewPager.adapter = labelPagerAdapter

        TabLayoutMediator(
            dialogBinding.labelTabLaout,
            dialogBinding.labelViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Choose"
                1 -> "Create"
                else -> null
            }
        }.attach()
        // Set width to 80% of screen width
        val layoutParams = layoutDialog.window?.attributes
        layoutParams?.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        layoutDialog.window?.attributes = layoutParams

        layoutDialog.show()
    }


    fun addEventToCalendar() {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "My Event")
            putExtra(CalendarContract.Events.EVENT_LOCATION, "Location")
            putExtra(CalendarContract.Events.DESCRIPTION, "Event Description")

            // Set start and end time
            val startTime = Calendar.getInstance().apply {
                set(2024, Calendar.OCTOBER, 25, 10, 0) // Year, Month, Day, Hour, Minute
            }.timeInMillis
            val endTime = Calendar.getInstance().apply {
                set(2024, Calendar.OCTOBER, 25, 12, 0)
            }.timeInMillis

            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            putExtra(CalendarContract.Events.ALL_DAY, false) // If it's an all-day event
            putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY) // User is busy during this event
        }

        startActivity(intent)
    }


    fun datePickerDialog(){
        // Get current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog
        val datePickerDialog = DatePickerDialog(requireContext() , { _, selectedYear, selectedMonth, selectedDay ->
            // Show selected date (note: month is 0-based, so add 1)
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()

            deadlineDate(selectedDate)

        }, year, month, day)

        // Show the DatePickerDialog
        datePickerDialog.show()
    }





}