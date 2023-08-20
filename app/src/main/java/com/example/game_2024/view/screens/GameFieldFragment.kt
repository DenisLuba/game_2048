package com.example.game_2024.view.screens

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout.LayoutParams
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game_2024.view_model.ModelFactory
import com.example.game_2024.R
import com.example.game_2024.view_model.ViewModel2024
import com.example.game_2024.databinding.FragmentGameFieldBinding
import com.example.game_2024.view.MainActivity
import com.example.game_2024.view.Tile
import com.example.game_2024.view.dialogs.ResetFragment
import kotlin.math.abs

class GameFieldFragment : Fragment() {

    private lateinit var binding: FragmentGameFieldBinding

    private lateinit var gameField: List<MutableList<Tile>>

    private lateinit var viewModel: ViewModel2024

    //    is game finished?
    private var isGameWon = false
    private var isGameLost = false

    private var score = 0
    private var maxScore = 0

    //    ( height, width ) of the game field
    private var dimensions = IntArray(2) { 4 }

    private var maxHeight = 4

    //    for the drawing of the field
    private val widthRelativeToScreen = 0.84
    private val heightRelativeToScreen = 1.092
    private var tileSize = 0.0
    private var fontSize = 0.0
    private var margin = 0

    private lateinit var linearLayout: LinearLayout
    private lateinit var linearLayoutBase: LinearLayout
    private val params = LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT,
        1.0f
    )
    private lateinit var textViews: Array<Array<TextView>>

    //    for gestures
    private lateinit var gestureDetector: GestureDetector
    private val touchListener: View.OnTouchListener by lazy {
        View.OnTouchListener { view, event ->
            view.performClick()
            gestureDetector.onTouchEvent(event)
        }
    }

//    **********************************************************************************************

    //    Overridden methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments?.getIntArray(MainActivity.DIMENSIONS) ?: intArrayOf(4, 4, 4)

        dimensions[0] = args[0]
        dimensions[1] = args[1]
        maxHeight = args[2]

        viewModel = ViewModelProvider(
            this,
            ModelFactory(requireActivity().application, args)
        )[ViewModel2024::class.java]
            .apply {
                liveDataField.observe(this@GameFieldFragment, fieldObserver)
                liveDataWinner.observe(this@GameFieldFragment, isWinnerObserver)
                liveDataLost.observe(this@GameFieldFragment, isLostObserver)
                liveDataScore.observe(this@GameFieldFragment, scoreObserver)
                liveDataMaxScore.observe(this@GameFieldFragment, maxScoreObserver)
            }

        gameField = List(dimensions.component1()) {
            MutableList(dimensions.component2()) { Tile(requireContext()) }
        } // initializing the playing field with zeros

        val displaySize = Resources
            .getSystem()
            .displayMetrics
            .widthPixels // width of display on pixels
        val widthOfFrame = displaySize * widthRelativeToScreen
        val heightOfFrame = displaySize * heightRelativeToScreen

        tileSize =
            if ((dimensions.component1().toDouble() / dimensions.component2()
                    .toDouble()) > 1.3
            ) // width of one Tile
                (heightOfFrame * 20) / (dimensions.component1() * 22)
            else (widthOfFrame * 20) / (dimensions.component2() * 22)

        fontSize = tileSize / 6
        margin = if (tileSize >= 40) (tileSize / 40).toInt() else 4 // margin between Tiles

        ResetFragment.setupListener(childFragmentManager, this) { reset() }
    }

//    **********************************************************************************************

    @SuppressLint("ClickableViewAccessibility") // FrameLayout.performClick()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        textViews = Array(dimensions.component1()) {
            Array(dimensions.component2()) {
                TextView(requireContext()).apply {
                    gravity = Gravity.CENTER
                    textSize = fontSize.toFloat()
                    width = tileSize.toInt()
                    height = tileSize.toInt()

                    layoutParams = params.apply {
                        requestLayout()
                        setMargins(margin, margin, margin, margin)
                    }
                }
            }
        }

        binding = FragmentGameFieldBinding.inflate(inflater, container, false).apply {

            undoButton.setOnClickListener { rollback() }
            frameGameField.setOnTouchListener(touchListener)
            homeButton.setOnClickListener {
                (activity as MainActivity).navController.navigate(R.id.action_gameFieldFragment_to_startFragment)
            }
            restartButton.setOnClickListener { ResetFragment.show(childFragmentManager) }
        }
        gestureDetector = GestureDetector(requireContext(), GestureListener(this))
        setFieldView()
        setTextViews()

        return binding.root
    }

//    **********************************************************************************************

    override fun onStop() {
        super.onStop()

        with(viewModel) {
            liveDataField.removeObserver(fieldObserver)
            liveDataWinner.removeObserver(isWinnerObserver)
            liveDataLost.removeObserver(isLostObserver)
            liveDataScore.removeObserver(scoreObserver)
        }
    }

//    **********************************************************************************************


    //    OBSERVERS FOR LIVEDATA

    private val fieldObserver: Observer<List<MutableList<Int>>> = Observer { field ->

        gameField.mapIndexed { i, list -> list.mapIndexed { j, tile -> tile.value = field[i][j] } }
//        setFieldView()
        setTextViews()
    }

    private val isWinnerObserver: Observer<Boolean> = Observer {
        isGameWon = it
    }

    private val isLostObserver: Observer<Boolean> = Observer {
        isGameLost = it
    }

    private val scoreObserver: Observer<Int> = Observer {
        score = it
        binding.score.text = getString(R.string.score, score)
    }

    private val maxScoreObserver: Observer<Int> = Observer {
        maxScore = it
        binding.highScore.text = getString(R.string.high_score, maxScore)
    }

//    **********************************************************************************************

    //    ACTIONS

    private fun reset() {
        viewModel.resetGame()
        isGameLost = false
        isGameWon = false
    }

    private fun rollback() {
        viewModel.rollback()
        isGameLost = false
        isGameWon = false
    }

    private fun move(direction: () -> Unit) {
        if (isGameLost || isGameWon) return
        direction.invoke()
    }

//    **********************************************************************************************

    //    Creating and rendering a tile table
    private fun setFieldView() {

        binding.gameField.removeAllViews()

        val heightOfField = dimensions.component1()
        val widthOfField = dimensions.component2()

        linearLayoutBase = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            weightSum = dimensions.component1().toFloat()
            foregroundGravity = Gravity.CENTER

            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        }

        for (i in 0 until heightOfField) {
            linearLayout = LinearLayout(context).apply {
                weightSum = widthOfField.toFloat()
                layoutParams = params.apply {
                    gravity = Gravity.CENTER
                }
            }
            for (j in 0 until widthOfField) {
//                textView = getTextView(gameField[i][j])
                linearLayout.addView(textViews[i][j])
            }
            linearLayoutBase.addView(linearLayout)
        }
        binding.gameField.addView(linearLayoutBase)
    }

    private fun setTextViews() {
        gameField.forEachIndexed { i, list ->
            list.forEachIndexed { j, tile ->
                with(textViews[i][j]) {
                    text = if (tile.value != 0) tile.value.toString() else ""
                    setTextColor(tile.getFontColor())
                    background =
                        ResourcesCompat.getDrawable(resources, R.drawable.tile, null).apply {
                            this?.overrideColor(tile.getTileColor())
                        }
                }
            }
        }
    }

//    **********************************************************************************************

    //    Drawable extension to set the background color of tiles
    private fun Drawable.overrideColor(@ColorInt colorInt: Int) {
        mutate()
        when (this) {
            is GradientDrawable -> setColor(colorInt)
            is ShapeDrawable -> paint.color = colorInt
            is ColorDrawable -> color = colorInt
        }
    }

//    **********************************************************************************************

    //    for gestures
    private class GestureListener(val fragment: GameFieldFragment) :
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
    }

//    **********************************************************************************************

    companion object {
        private const val SWIPE_MIN_DISTANCE = 100
        private const val SWIPE_THRESHOLD_VELOCITY = 100
    }
}