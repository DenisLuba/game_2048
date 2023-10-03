package com.example.game_2024.view.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.example.game_2024.R
import com.example.game_2024.databinding.FragmentStartBinding
import com.example.game_2024.view.MainActivity
import com.example.game_2024.view.dialogs.ExitDialog
import com.google.gson.Gson

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private lateinit var preferences: SharedPreferences
    private var dimensions = IntArray(2) { 4 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        preferences = requireContext().getSharedPreferences(START_PREFERENCES, Context.MODE_PRIVATE)
        val preferencesDimensions: String? = preferences.getString(DIMENSIONS_PREFERENCES, null)
        if (preferencesDimensions != null) dimensions = gsonToDimensions(preferencesDimensions)

        binding = FragmentStartBinding.inflate(inflater, container, false).apply {

            (numberPickerHeight as NumberPicker).minValue = MIN_HEIGHT
            (numberPickerWidth as NumberPicker).minValue = MIN_WIDTH
            numberPickerHeight.maxValue = MAX_HEIGHT
            numberPickerWidth.maxValue = MAX_WIDTH

            numberPickerHeight.value = dimensions[0]
            numberPickerWidth.value = dimensions[1]

            overrideFont(requireContext(), numberPickerHeight)

            startButton.setOnClickListener { startButtonOnClick() }
            exitButton.setOnClickListener { ExitDialog.show(childFragmentManager) }
            soundButton.setOnClickListener {
                MainActivity.musicSwitcher(requireActivity(), it as ConstraintLayout)
            }
            MainActivity.changeMusicIconState(requireActivity(), soundButton)
        }

        return binding.root
    }

    private fun startButtonOnClick() {
        with(binding) {
            dimensions[0] = (numberPickerHeight as NumberPicker).value
            dimensions[1] = (numberPickerWidth as NumberPicker).value
        }


        val gsonDimensions: String = dimensionsToGson(dimensions)
        preferences.edit().putString(DIMENSIONS_PREFERENCES, gsonDimensions).apply()

        val bundle = bundleOf(
            MainActivity.DIMENSIONS to
                    intArrayOf(dimensions.component1(), dimensions.component2(), MAX_HEIGHT)
        )

//        navigate to GameFieldFragment
        (activity as MainActivity)
            .navController
            .navigate(R.id.action_startFragment_to_gameFieldFragment, bundle)
    }

    //    Additional methods
    private fun gsonToDimensions(value: String): IntArray =
        Gson().fromJson(value, IntArray::class.java)

    private fun dimensionsToGson(array: IntArray): String = Gson().toJson(array)

    private fun overrideFont(context: Context, view: View?) {
        if (view is ViewGroup) {
            for (i in 0..view.childCount) {
                val child: View? = view.getChildAt(i)
                overrideFont(context, child)
            }
        } else if (view is EditText) {
            view.typeface = ResourcesCompat.getFont(context, R.font.default_font)
        }
    }

    companion object {
        private const val START_PREFERENCES = "START_PREFERENCES"
        private const val DIMENSIONS_PREFERENCES = "DIMENSIONS_PREFERENCES"
        private const val MAX_HEIGHT = 10
        private const val MAX_WIDTH = 10
        private const val MIN_HEIGHT = 2
        private const val MIN_WIDTH = 2
    }
}