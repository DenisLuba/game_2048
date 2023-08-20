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
import com.example.game_2024.view.dialogs.selectDimensions.SelectFieldFragment

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    private var dimensions = intArrayOf(4, 4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dimensions = arguments?.getIntArray(DIMENSIONS) ?: intArrayOf(4, 4)
        SelectFieldFragment.setupListener(childFragmentManager, this) {
            dimensions = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false).apply {

            startButton.setOnClickListener {
                val bundle = bundleOf(MainActivity.DIMENSIONS to intArrayOf(dimensions.component1(), dimensions.component2(), maxHeight))

                (activity as MainActivity)
                    .navController
                    .navigate(R.id.action_startFragment_to_gameFieldFragment, bundle)
            }

            chooseSizeButton.setOnClickListener {
                val selectField = SelectFieldFragment()

                val values = intArrayOf(dimensions[0], dimensions[1], maxHeight, maxWidth)
                val bundle = bundleOf(SelectFieldFragment.VALUES to values)
                selectField.arguments = bundle
                selectField.show(childFragmentManager, SelectFieldFragment.REQUEST_KEY)
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(DIMENSIONS, dimensions)
    }

    companion object{
        private const val DIMENSIONS = "DIMENSIONS"
        private const val maxHeight = 26
        private const val maxWidth = 20
    }
}