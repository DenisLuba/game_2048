package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.game_2024.R
import com.example.game_2024.databinding.FragmentExitDialogBinding
import com.example.game_2024.view.screens.GameFieldFragment

class ResetDialog : DialogFragment() {

    private lateinit var binding: FragmentExitDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentExitDialogBinding.inflate(layoutInflater).apply {
                exitOrRestartTextView.text = resources.getText(R.string.dialog_restart)
                yesButton.setOnClickListener {
                    resetClick(YES)
                    dismiss()
                }
                noButton.setOnClickListener {
                    resetClick(NO)
                    dismiss()
                }
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

    private fun resetClick(reply: String) {
        parentFragmentManager.setFragmentResult(
            GameFieldFragment.REQUEST_KEY,
            bundleOf(GameFieldFragment.RESULT to reply)
        )
    }

    companion object {
        const val YES = "YES"
        const val NO = "NO"

        private fun newInstance() = ResetDialog()

        fun show(manager: FragmentManager) = newInstance().show(manager, GameFieldFragment.REQUEST_KEY)
    }
}