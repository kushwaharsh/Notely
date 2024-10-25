package com.example.notesappwithhilt.ui.home.label

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.notesappwithhilt.commonUtils.BlurDialog

class LabelPagerAdapter(
    fragmentActivity: FragmentActivity,
    val layoutDialog: BlurDialog,
    var labelName: (String) -> Unit,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddLabelFragment(layoutDialog) {
                labelName(it)
            }

            1 -> CreateLabelFragment(layoutDialog) {
                labelName(it)
            }

            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
