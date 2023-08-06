package com.example.game_2024.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game_2024.MainActivity
import com.example.game_2024.ModelFactory
import com.example.game_2024.R
import com.example.game_2024.ViewModel2024
import com.example.game_2024.databinding.FragmentGameFieldBinding

class GameFieldFragment : Fragment() {

    private lateinit var binding: FragmentGameFieldBinding
    private lateinit var viewModel: ViewModel2024
    private val observer: Observer<Int> = Observer {

    }

    private var dimensions = intArrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dimensions = arguments?.getIntArray(MainActivity.DIMENSIONS) ?: intArrayOf(4, 4)
        viewModel = ViewModelProvider(this, ModelFactory(requireActivity().application, dimensions))[ViewModel2024::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGameFieldBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.liveData.observe(this, observer)

        binding.homeButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_gameFieldFragment_to_startFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.liveData.removeObserver(observer)
    }
}