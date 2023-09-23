package com.example.game_2024.view

import android.view.GestureDetector
import android.view.MotionEvent
import com.example.game_2024.view.screens.GameFieldFragment
import kotlin.math.abs

class GestureListener private constructor(val fragment: GameFieldFragment) :
    GestureDetector.SimpleOnGestureListener() {

    override fun onDown(e: MotionEvent) = true

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (event1.x - event2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            fragment.move { fragment.viewModel.left() }
        else if (event2.x - event1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            fragment.move { fragment.viewModel.right() }
        else if (event1.y - event2.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            fragment.move { fragment.viewModel.up() }
        else if (event2.y - event1.y > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            fragment.move { fragment.viewModel.down() }

        return false
    }

    companion object {
        private const val SWIPE_MIN_DISTANCE = 120
        private const val SWIPE_THRESHOLD_VELOCITY = 100

        private var instance: GestureListener? = null

        fun getInstance(fragment: GameFieldFragment): GestureListener =
            if (instance != null) {
                synchronized(this) {
                    if (instance != null) instance!!
                    else GestureListener(fragment)
                }
            } else synchronized(this) {
                GestureListener(fragment)
            }
    }
}
