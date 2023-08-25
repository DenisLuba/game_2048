package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.game_2024.databinding.FragmentGameOverDialogBinding
import com.example.game_2024.view.screens.GameFieldFragment

class GameOverDialog : DialogFragment() {

    private lateinit var binding: FragmentGameOverDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentGameOverDialogBinding.inflate(layoutInflater).apply {
                gaveOverTextView.text = endText
                root.setOnClickListener{
                    when (endText) {
                        GameFieldFragment.WIN -> resetAfterWinning(WIN_KEY)
                        GameFieldFragment.GAME_OVER -> resetAfterWinning(GAME_OVER_KEY)
                    }
                    dismiss()
                }
            }
            builder.setView(binding.root)
                .setCancelable(true)
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
    }

    private fun resetAfterWinning(requestTag: String) {
        parentFragmentManager.setFragmentResult(
            GameFieldFragment.REQUEST_KEY,
            bundleOf(GameFieldFragment.RESULT to requestTag)
        )
    }

    companion object {
        private var endText: String = ""

        const val GAME_OVER_KEY = "GAME_OVER"
        const val WIN_KEY = "WIN"

        private fun newInstance() = GameOverDialog()

        fun showGameOver(manager: FragmentManager, requestTag: String) {
            endText = requestTag
            newInstance().show(manager, requestTag)
        }
    }
}