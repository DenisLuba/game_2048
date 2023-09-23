package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.game_2024.R
import com.example.game_2024.databinding.FragmentExitDialogBinding
import kotlin.system.exitProcess

class ExitDialog : DialogFragment() {

    private lateinit var binding: FragmentExitDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentExitDialogBinding.inflate(layoutInflater).apply {
                exitOrRestartTextView.text = resources.getText(R.string.dialog_exit)
                yesButton.setOnClickListener {
                    requireActivity().finish()
                    exitProcess(0)
                }
                noButton.setOnClickListener {
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

    companion object {

        private const val REQUEST_KEY_EXIT = "REQUEST_KEY"
        private fun newInstance() = ExitDialog()

        fun show(manager: FragmentManager) =
            newInstance().show(manager, REQUEST_KEY_EXIT)
    }
}