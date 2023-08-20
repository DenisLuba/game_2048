package com.example.game_2024.view.dialogs.selectDimensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game_2024.databinding.FragmentSelectFieldBinding
import com.example.game_2024.view.MainActivity
import com.example.game_2024.view.dialogs.ResetFragment
import com.example.game_2024.view.screens.StartFragment

class SelectFieldFragment() : DialogFragment() {

    private lateinit var binding: FragmentSelectFieldBinding

    private var dimensions = intArrayOf(4, 4)
    private var maxHeight = 4
    private var maxWidth = 4

    private var width = 4
    private var height = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null && !requireArguments().isEmpty) {
            height = requireArguments().getIntArray(VALUES)?.get(0) ?: 4
            width = requireArguments().getIntArray(VALUES)?.get(1) ?: 4
            maxHeight = requireArguments().getIntArray(VALUES)?.get(2) ?: 4
            maxWidth = requireArguments().getIntArray(VALUES)?.get(3) ?: 4
        }

        Log.d("MyTAG", "onCreate(1): height = $height, width = $width, maxHeight = $maxHeight, maxWidth = $maxWidth")

        if ((savedInstanceState != null) && !savedInstanceState.isEmpty) {
            height = savedInstanceState.getIntArray(VALUES)?.get(0) ?: 4
            width = savedInstanceState.getIntArray(VALUES)?.get(1) ?: 4
            maxHeight = savedInstanceState.getIntArray(VALUES)?.get(2) ?: 4
            maxWidth = savedInstanceState.getIntArray(VALUES)?.get(3) ?: 4
        }


        Log.d("MyTAG", "onCreate(2): height = $height, width = $width, maxHeight = $maxHeight, maxWidth = $maxWidth")
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            binding = FragmentSelectFieldBinding.inflate(layoutInflater)

            with (binding) {
                numberPickerHeight.minValue = 1
                numberPickerHeight.maxValue = maxHeight
                numberPickerWidth.minValue = 1
                numberPickerWidth.maxValue = maxWidth

                numberPickerHeight.value = height
                numberPickerWidth.value = width

                okSelectDimensions.setOnClickListener {
                    dimensions[0] = numberPickerHeight.value
                    dimensions[1] = numberPickerWidth.value
                    sendDimensions(dimensions)
                    dismiss()
                }
                cancelSelectDimensions.setOnClickListener { dismiss() }
            }

            builder.setView(binding.root)
                .setCancelable(true)
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onSaveInstanceState(outState: Bundle) {

        Log.d("MyTAG", "onSaveInstanceState: height = $height, width = $width, maxHeight = $maxHeight, maxWidth = $maxWidth")

        super.onSaveInstanceState(outState)
        outState.putIntArray(VALUES, intArrayOf(binding.numberPickerHeight.value, binding.numberPickerWidth.value, maxHeight, maxWidth))
    }

    private fun sendDimensions(dimensions: IntArray) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT to dimensions)
        )
    }

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
        const val RESULT = "RESULT"
        const val VALUES = "VALUES"

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (IntArray) -> Unit
        ) {
            manager.setFragmentResultListener(ResetFragment.REQUEST_KEY, lifecycleOwner) { _, bundle ->
                listener.invoke(bundle.getIntArray(RESULT) ?: intArrayOf(4, 4))
            }
        }
    }

}