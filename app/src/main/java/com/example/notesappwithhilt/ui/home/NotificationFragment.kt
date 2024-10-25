package com.example.notesappwithhilt.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.databinding.FragmentNotificationBinding
import com.suke.widget.SwitchButton


class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentNotificationBinding.inflate(inflater , container , false)
        
        initview()
        
        return binding.root
    }

    private fun initview() {
        binding.switchButton.apply {
            isChecked = true
            toggle() // Switch state
            toggle(false) // Switch without animation
            setShadowEffect(true) // Disable shadow effect
            isEnabled = true // Enable button
            setEnableEffect(true) // Enable the switch animation

            setOnCheckedChangeListener { view, isChecked ->
                if (!isChecked) {
                    Toast.makeText(requireContext(), "Notifications are turned off", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Notifications are turned on", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

}