package com.example.notesappwithhilt.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesappwithhilt.R
import com.example.notesappwithhilt.commonUtils.App
import com.example.notesappwithhilt.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater , container , false)

        initview()
        loader()

        return binding.root
    }

    private fun initview() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (App.app.prefManager.isLoggedIn){
               // findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 2000)
    }

    private fun loader() {
        // Set up the VideoView using View Binding
        val videoView = binding.videoLoader

        // Assuming you have the video in the res/raw folder (e.g., loader_video.mp4)
        val videoUri = Uri.parse("android.resource://" + (context?.packageName ?: "") + "/" + R.raw.note_loader)
        videoView.setVideoURI(videoUri)

        // Loop the video
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        videoView.start()

    }

}