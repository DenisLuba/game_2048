package com.example.game_2024.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.game_2024.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ButtonsAnimation {

    private var pitHome = true
    private var pitUndo = false
    private var pitReset = false

    private var distanceForButtons = 0f
    private var toUndo = 0f
    private var toReset = 0f

    private var displayHeight = 0f
    private var displayWidth = 0f

    private const val sizeChangeFactor = 1.2f
    private const val animationDuration = 800L

    private const val home_to_reset = "home_to_reset"
    private const val reset_to_home = "reset_to_home"
    private const val home_to_undo = "home_to_undo"
    private const val undo_to_home = "undo_to_home"
    private const val undo_to_reset = "undo_to_reset"
    private const val reset_to_undo = "reset_to_undo"

    const val home = "home"
    const val undo = "undo"
    const val reset = "reset"

    init {
        displayHeight = Resources
            .getSystem()
            .displayMetrics
            .heightPixels.toFloat()

        distanceForButtons = displayHeight * 0.03f

        displayWidth = Resources
            .getSystem()
            .displayMetrics
            .widthPixels.toFloat()

        toUndo = displayWidth * 0.605f
        toReset = displayWidth * 0.822f
    }

    fun start(activity: Activity, view: View, up: Boolean) {
        MainActivity.mainScope.launch {
            view.verticalShift(up, time = 0, activity)
        }
    }

    fun animationShift(
        activity: Activity,
        buttonHome: View,
        buttonUndo: View,
        buttonReset: View,
        pit: View,
        button: String
    ) {

        MainActivity.mainScope.launch(Dispatchers.Main) {

            when (button.trim().lowercase()) {
                home -> {
                    if (pitUndo) {
                        replacingButtons(buttonHome, buttonUndo, home, undo, activity)
                        pit.horizontalShift(undo_to_home, activity)
                    } else if (pitReset) {
                        replacingButtons(buttonHome, buttonReset, home, reset, activity)
                        pit.horizontalShift(reset_to_home, activity)
                    }

//                home
                }

                undo -> {
                    if (pitHome) {
                        replacingButtons(buttonUndo, buttonHome, undo, home, activity)
                        pit.horizontalShift(home_to_undo, activity)
                    } else if (pitReset) {
                        replacingButtons(buttonUndo, buttonReset, undo, reset, activity)
                        pit.horizontalShift(reset_to_undo, activity)
                    }

//                undo
                }

                reset -> {
                    if (pitHome) {
                        replacingButtons(buttonReset, buttonHome, reset, home, activity)
                        pit.horizontalShift(home_to_reset, activity)
                    } else if (pitUndo) {
                        replacingButtons(buttonReset, buttonUndo, reset, undo, activity)
                        pit.horizontalShift(undo_to_reset, activity)
                    }

//                reset
                }
            }
        }
    }

    private fun replacingButtons(
        buttonUp: View,
        buttonDown: View,
        buttonUpString: String,
        buttonDownString: String,
        activity: Activity
    ) {

        buttonUp.verticalShift(up = true, time = animationDuration, activity)
        buttonDown.verticalShift(up = false, time = animationDuration, activity)

        when (buttonUpString) {
            home -> pitHome = true
            undo -> pitUndo = true
            reset -> pitReset = true
        }

        when (buttonDownString) {
            home -> pitHome = false
            undo -> pitUndo = false
            reset -> pitReset = false
        }
    }

    private fun View.verticalShift(up: Boolean, time: Long, activity: Activity) {

        val distance: Float
        val targetColor: Int
        val ratio: Float
        if (up) {
            distance = -distanceForButtons
            targetColor = R.color.color_background_2
            ratio = sizeChangeFactor
        } else {
            distance = distanceForButtons
            targetColor = R.color.color_background_1
            ratio = 1 / sizeChangeFactor
        }

        val image: ImageView = (this@verticalShift as ViewGroup).getChildAt(0) as ImageView

        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            animate()
                .translationX(distance)
                .withStartAction {
                    changeImage(image, activity, ratio, time, targetColor)
                }.duration = time

        } else {

            animate()
                .translationY(distance)
                .withStartAction {
                    changeImage(image, activity, ratio, time, targetColor)
                }.duration = time
        }
    }

    private fun changeImage(
        image: ImageView,
        activity: Activity,
        ratio: Float,
        time: Long,
        targetColor: Int
    ) {
        image
            .animate()
            .scaleX(ratio)
            .scaleY(ratio)
            .withStartAction {

                image
                    .animate()
                    .alpha(0f)
                    .setDuration(time / 2)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            image
                                .setColorFilter(
                                    activity.resources.getColor(
                                        targetColor,
                                        activity.theme
                                    )
                                )

                            image
                                .animate().alpha(1f).duration = time / 2
                        }
                    })
            }.duration = time

    }

// moving pit

    private fun View.horizontalShift(to: String, activity: Activity) {
        val animationId: Int
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            animationId = when (to) {
                home_to_reset -> R.anim.home_to_reset_land
                reset_to_home -> R.anim.reset_to_home_land
                home_to_undo -> R.anim.home_to_undo_land
                undo_to_home -> R.anim.undo_to_home_land
                undo_to_reset -> R.anim.undo_to_reset_land
                reset_to_undo -> R.anim.reset_to_undo_land
                else -> -1
            }
        } else {

            animationId = when (to) {
                home_to_reset -> R.anim.home_to_reset
                reset_to_home -> R.anim.reset_to_home
                home_to_undo -> R.anim.home_to_undo
                undo_to_home -> R.anim.undo_to_home
                undo_to_reset -> R.anim.undo_to_reset
                reset_to_undo -> R.anim.reset_to_undo
                else -> -1
            }
        }

        val animation: Animation = AnimationUtils.loadAnimation(activity, animationId)
        startAnimation(animation)
    }
}