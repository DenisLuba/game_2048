package com.example.game_2024.view.dialogs.selectDimensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

    private val listWidth: List<Int> get() = (1..maxWidth).toList()
    private val listHeight: List<Int> get() = (1..maxHeight).toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dimensions = savedInstanceState?.getIntArray(RESULT) ?: intArrayOf(4, 4)
        maxHeight = savedInstanceState?.getIntArray(SIZE_LIMITS)?.get(0) ?: 4
        maxWidth = savedInstanceState?.getIntArray(SIZE_LIMITS)?.get(1) ?: 4

        maxHeight = arguments?.getIntArray(SIZE_LIMITS)?.get(0) ?: 4
        maxWidth = arguments?.getIntArray(SIZE_LIMITS)?.get(1) ?: 4
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
        super.onSaveInstanceState(outState)
        outState.putIntArray(RESULT, dimensions)
        outState.putIntArray(SIZE_LIMITS, intArrayOf(maxHeight, maxWidth))
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

        const val SIZE_LIMITS = "SIZE LIMITS"

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