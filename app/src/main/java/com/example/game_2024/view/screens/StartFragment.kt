package com.example.game_2024.view.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.game_2024.R
import com.example.game_2024.databinding.FragmentStartBinding
import com.example.game_2024.view.MainActivity

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    private val maxHeight = 26
    private val maxWidth = 20
    private var dimensions = intArrayOf(2, 2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.startButton.setOnClickListener {

            val bundle = bundleOf(MainActivity.DIMENSIONS to dimensions)
            (activity as MainActivity)
                .navController
                .navigate(R.id.action_startFragment_to_gameFieldFragment, bundle)
        }
    }
}