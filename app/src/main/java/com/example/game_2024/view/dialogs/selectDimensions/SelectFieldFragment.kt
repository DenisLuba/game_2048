package com.example.game_2024.view.dialogs.selectDimensions

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game_2024.databinding.FragmentSelectFieldBinding
import com.example.game_2024.view.dialogs.ResetFragment
import com.example.game_2024.view.screens.StartFragment

class SelectFieldFragment(private val startFragment: StartFragment) : DialogFragment() {

    private lateinit var binding: FragmentSelectFieldBinding

    private var dimensions = intArrayOf(4, 4)

    private lateinit var layoutManagerWidth: LinearLayoutManager
    private lateinit var layoutManagerHeight: LinearLayoutManager

    private lateinit var adapterWidth: AdapterForSelectFieldFragment
    private lateinit var adapterHeight: AdapterForSelectFieldFragment

    private val listWidth: List<Int> get() = (1..startFragment.maxWidth).toList()
    private val listHeight: List<Int> get() = (1..startFragment.maxHeight).toList()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dimensions = savedInstanceState?.getIntArray(RESULT) ?: intArrayOf(4, 4)
        binding = FragmentSelectFieldBinding.inflate(layoutInflater)

        layoutManagerWidth = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
            scrollToPosition((Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % listWidth.size))
        }
        layoutManagerHeight = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
            scrollToPosition((Integer.MAX_VALUE / 2) - ((Integer.MAX_VALUE / 2) % listHeight.size))
        }

        binding.recyclerViewWidth.layoutManager = layoutManagerWidth
        binding.recyclerViewHeight.layoutManager = layoutManagerHeight

        adapterWidth = AdapterForSelectFieldFragment(startFragment, false, listWidth)
        adapterHeight = AdapterForSelectFieldFragment(startFragment, true, listHeight)

        binding.recyclerViewWidth.adapter = adapterWidth
        binding.recyclerViewHeight.adapter = adapterHeight


        /*
        TODO

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)
                .setCancelable(true)
                .setSingleChoiceItems()
            builder.create()
        } ?: throw IllegalStateException("FragmentActivity cannot be null")

         */
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