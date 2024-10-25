package com.example.notesappwithhilt.ui.introScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.databinding.FragmentPremiumBinding


class PremiumFragment : Fragment() {

    private lateinit var binding: FragmentPremiumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPremiumBinding.inflate(inflater , container , false)

        listener()

        return binding.root
    }

    private fun listener() {

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.annualPriceLayout.setOnClickListener{
            binding.annualPriceLayout.setBackgroundResource(R.drawable.card_rounder_style)
            binding.monthlyPriceLayout.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        binding.monthlyPriceLayout.setOnClickListener{
            binding.monthlyPriceLayout.setBackgroundResource(R.drawable.card_rounder_style)
            binding.annualPriceLayout.setBackgroundResource(R.drawable.rounded_border_grey)
        }
    }

}