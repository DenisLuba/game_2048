package com.example.game_2024.view.dialogs.selectDimensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game_2024.databinding.ItemSelectFieldBinding
import com.example.game_2024.view.screens.StartFragment

class AdapterForSelectFieldFragment(
    private val startFragment: StartFragment,
    private val isHeight: Boolean,
    private val list: List<Int>
) :
    RecyclerView.Adapter<AdapterForSelectFieldFragment.SelectFieldViewHolder>() {

    override fun getItemCount() = Integer.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectFieldViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectFieldBinding.inflate(inflater, parent, false)

        return SelectFieldViewHolder(binding, startFragment, isHeight)
    }

    override fun onBindViewHolder(holder: SelectFieldViewHolder, position: Int) {
        val positionInList = position % list.size
        holder.dimension.text = list[positionInList].toString()
    }

    class SelectFieldViewHolder(
        binding: ItemSelectFieldBinding,
        startFragment: StartFragment,
        isHeight: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        val dimension = binding.itemSelectTextView

        init {
            binding.root.setOnClickListener {
                if (isHeight) {
                    startFragment.dimensions[0] =
                        binding.itemSelectTextView.text.toString().toIntOrNull() ?: 4
                } else
                    startFragment.dimensions[1] =
                        binding.itemSelectTextView.text.toString().toIntOrNull() ?: 4
            }
        }
    }
}