package com.example.game_2024.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.game_2024.databinding.FragmentGameOverBinding
import com.example.game_2024.view.screens.GameFieldFragment

class GameOverDialog : DialogFragment() {

    private lateinit var binding: FragmentGameOverBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentGameOverBinding.inflate(layoutInflater).apply {
                gaveOverTextView.text = endText
                root.setOnClickListener{
                    dismiss()
                }
            }
            builder.setView(binding.root)
                .setCancelable(true)
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
    }



    companion object {
        private var endText: String = ""

        private const val GAME_OVER = "Game over..."
        private const val WIN = "You win!"
        private const val GAME_OVER_KEY = "GAME_OVER"
        private const val WIN_KEY = "WIN"


        private fun newInstance() = GameOverDialog()

        fun showGameOver(manager: FragmentManager, requestTag: String) {
            endText = when (requestTag) {
                GAME_OVER_KEY -> GAME_OVER
                WIN_KEY -> WIN
                else -> "Wrong"
            }
            newInstance().show(manager, requestTag)
        }

//        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: () -> Unit) {
//            manager.setFragmentResultListener(ResetFragment.REQUEST_KEY, lifecycleOwner) { _, bundle ->
//                when (bundle.getString(ResetFragment.RESULT)) {
//                    ResetFragment.YES -> listener.invoke()
////                    NO -> nothing
//                }
//            }
//        }
    }
}