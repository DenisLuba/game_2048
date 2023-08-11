package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.game_2024.databinding.FragmentResetBinding

class ResetFragment : DialogFragment() {

    private lateinit var binding: FragmentResetBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentResetBinding.inflate(layoutInflater).apply {

                yesRestartButton.setOnClickListener {
                    resetClick(YES)
                    dismiss()
                }
                noRestartButton.setOnClickListener {
                    resetClick(NO)
                    dismiss()
                }
            }
//            val inflater = requireActivity().layoutInflater

            builder.setView(binding.root)
                .setCancelable(true)
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
    }

    private fun resetClick(reply: String) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT to reply)
        )
    }

    companion object {
        const val REQUEST_KEY = "DIALOG_REQUEST_PARAM"
        const val RESULT = "RESULT"
        const val YES = "YES"
        const val NO = "NO"

        @JvmStatic
        fun newInstance(): DialogFragment {
            val dialog = ResetFragment()

            return dialog
        }
    }

}