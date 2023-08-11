package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.game_2024.R
import com.example.game_2024.databinding.FragmentResetBinding
import kotlin.ClassCastException

class ResetFragment : DialogFragment() {

    private lateinit var binding: FragmentResetBinding

    internal lateinit var listener: ResetFragmentListener

    interface ResetFragmentListener {
        fun onDialogResetPositiveClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ResetFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ResetFragmentListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentResetBinding.inflate(layoutInflater).apply {
                yesRestartButton.setOnClickListener { listener.onDialogResetPositiveClick(this@ResetFragment) }
                noRestartButton.setOnClickListener { dialog?.cancel() }
            }
            val inflater = requireActivity().layoutInflater

            builder.setView(binding.root)
                .setCancelable(true)
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
    }


}