package com.example.game_2024.view.screens

import android.content.Context
import android.content.SharedPreferences
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

    private var dimensions = IntArray(2) { 4 }

//    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dimensions = savedInstanceState?.getIntArray(DIMENSIONS) ?: IntArray(2) { 4 }
        SelectFieldFragment.setupListener(childFragmentManager, this) {
            dimensions = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        preferences = requireContext().getSharedPreferences(START_PREFERENCES, Context.MODE_PRIVATE)
//        dimensions = preferences
//            .getStringSet(DIMENSIONS_PREFERENCES, null)
//            ?.toList()
//            ?.map { it.toInt() }
//            ?.toIntArray()
//            ?: dimensions

        binding = FragmentStartBinding.inflate(inflater, container, false).apply {

            numberPickerHeight.minValue = 1
            numberPickerHeight.maxValue = maxHeight
            numberPickerWidth.minValue = 1
            numberPickerWidth.maxValue = maxWidth

            numberPickerHeight.value = dimensions[0]
            numberPickerWidth.value = dimensions[1]

            startButton.setOnClickListener {
                dimensions[0] = numberPickerHeight.value
                dimensions[1] = numberPickerWidth.value

                val bundle = bundleOf(MainActivity.DIMENSIONS to intArrayOf(dimensions.component1(), dimensions.component2(), maxHeight))

                (activity as MainActivity)
                    .navController
                    .navigate(R.id.action_startFragment_to_gameFieldFragment, bundle)
            }



//            chooseSizeButton.setOnClickListener {
//                val selectField = SelectFieldFragment()
//
//                val values = intArrayOf(dimensions[0], dimensions[1], maxHeight, maxWidth)
//                val bundle = bundleOf(SelectFieldFragment.VALUES to values)
//                selectField.arguments = bundle
//                selectField.show(childFragmentManager, SelectFieldFragment.REQUEST_KEY)
//            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dimensions[0] = binding.numberPickerHeight.value
        dimensions[1] = binding.numberPickerWidth.value
        outState.putIntArray(DIMENSIONS, dimensions)
    }

    companion object{
        private const val START_PREFERENCES = "START_PREFERENCES"
        private const val DIMENSIONS_PREFERENCES = "DIMENSIONS_PREFERENCES"
        private const val DIMENSIONS = "DIMENSIONS"
        private const val maxHeight = 26
        private const val maxWidth = 20
    }
}