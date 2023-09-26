package com.example.game_2024.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.example.game_2024.R

class CustomNumberPicker(
    context: Context,
    attributeSet: AttributeSet
) : android.widget.NumberPicker(context, attributeSet) {

    private var type: Typeface?

    init {
        type = ResourcesCompat.getFont(context, R.font.dela_gothic_one)
    }

    override fun addView(child: View?) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(
        child: View?,
        params: android.view.ViewGroup.LayoutParams
    ) {
        super.addView(child, params)
        updateView(child)
    }

    override fun addView(
        child: View?,
        index: Int,
        params: android.view.ViewGroup.LayoutParams
    ) {
        super.addView(child, index, params)
        updateView(child)
    }

    private fun updateView(view: View?) {
        if (view is EditText) {
            view.typeface = ResourcesCompat.getFont(context, R.font.dela_gothic_one)
            view.setTextColor(resources.getColor(R.color.number_picker_text_color, null))
        }
    }
}