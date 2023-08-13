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
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
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
            REQUEST_KEY,
            bundleOf(RESULT to reply)
        )
    }

    companion object {
        @JvmStatic val REQUEST_KEY = "REQUEST_KEY"
        @JvmStatic val RESULT = "RESULT"
        @JvmStatic val YES = "YES"
        @JvmStatic val NO = "NO"

        @JvmStatic
        fun newInstance() = ResetFragment()

        fun show(manager: FragmentManager) = newInstance().show(manager, REQUEST_KEY)

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: () -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, bundle ->
                when (bundle.getString(RESULT)) {
                    YES -> listener.invoke()
//                    NO -> nothing
                }
            }
        }
    }

}