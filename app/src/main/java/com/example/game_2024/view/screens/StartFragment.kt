package com.example.game_2024.view.screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

            numberPickerHeight.minValue = 2
            numberPickerHeight.maxValue = maxHeight
            numberPickerWidth.minValue = 2
            numberPickerWidth.maxValue = maxWidth

            numberPickerHeight.value = dimensions[0]
            numberPickerWidth.value = dimensions[1]

            startButton.setOnClickListener { startButtonOnClick() }
            exitButton.setOnClickListener { ExitDialog.show(childFragmentManager) }

            changeMusicState(soundButton)
            soundButton.setOnClickListener {
                MainActivity.musicOn = !MainActivity.musicOn
                changeMusicState(it as ConstraintLayout)
            }
        }

        return binding.root
    }

    private fun changeMusicState(view: ConstraintLayout) {
        if (MainActivity.musicOn) {
            ((view as ViewGroup).getChildAt(0) as ImageView).setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.music_default, null)
            )
        } else ((view as ViewGroup).getChildAt(0) as ImageView).setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.no_music, null)
        )
        MainActivity.startStopMusic()
    }

    private fun startButtonOnClick() {
        dimensions[0] = binding.numberPickerHeight.value
        dimensions[1] = binding.numberPickerWidth.value

        val gsonDimensions: String = dimensionsToGson(dimensions)
        preferences.edit().putString(DIMENSIONS_PREFERENCES, gsonDimensions).apply()

        val bundle = bundleOf(
            MainActivity.DIMENSIONS to
                    intArrayOf(dimensions.component1(), dimensions.component2(), maxHeight)
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

    companion object {
        private const val START_PREFERENCES = "START_PREFERENCES"
        private const val DIMENSIONS_PREFERENCES = "DIMENSIONS_PREFERENCES"
        private const val maxHeight = 10
        private const val maxWidth = 10
    }
}