package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.example.game_2024.databinding.FragmentResetBinding
import com.example.game_2024.databinding.FragmentSelectFieldBinding

class SelectFieldFragment : DialogFragment() {

    private lateinit var binding: FragmentSelectFieldBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentSelectFieldBinding.inflate(layoutInflater).apply {


            }
            builder.setView(binding.root)
                .setCancelable(true)
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
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

        const val WIDTH = "WIDTH"
        const val HEIGHT = "HEIGHT"

        @JvmStatic
        fun newInstance() = SelectFieldFragment()
    }

}