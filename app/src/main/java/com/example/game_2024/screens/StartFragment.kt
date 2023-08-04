package com.example.game_2024.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.game_2024.MainActivity
import com.example.game_2024.R
import com.example.game_2024.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.startButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_startFragment_to_gameFieldFragment)
        }
    }
}