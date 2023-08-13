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
import androidx.lifecycle.LifecycleOwner
import com.example.game_2024.databinding.FragmentResetBinding
import com.example.game_2024.databinding.FragmentSelectFieldBinding

class SelectFieldFragment : DialogFragment() {

    private lateinit var binding: FragmentSelectFieldBinding

    private var dimensions = intArrayOf(4, 4)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dimensions = savedInstanceState?.getIntArray(RESULT) ?: intArrayOf(4, 4)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentSelectFieldBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setCancelable(true)
                .setSingleChoiceItems()
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(RESULT, dimensions)
    }

    private fun sendDimensions(dimensions: IntArray) {
        childFragmentManager.setFragmentResult(
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

        fun show(manager: FragmentManager) = ResetFragment.newInstance()
            .show(manager, ResetFragment.REQUEST_KEY)

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (IntArray) -> Unit) {
            manager.setFragmentResultListener(ResetFragment.REQUEST_KEY, lifecycleOwner) { _, bundle ->
                listener.invoke(bundle.getIntArray(RESULT) ?: intArrayOf(4, 4))
            }
        }
    }

}