package com.example.game_2024.view

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game_2024.view_model.ModelFactory
import com.example.game_2024.R
import com.example.game_2024.view_model.ViewModel2024
import com.example.game_2024.databinding.FragmentGameFieldBinding
import kotlin.math.abs

class GameFieldFragment : Fragment() {

    private lateinit var binding: FragmentGameFieldBinding

    private lateinit var gameField: List<MutableList<Tile>>

    private lateinit var viewModel: ViewModel2024

    //    is game finished?
    private var isGameWon = false
    private var isGameLost = false

    private var score = 0

    //    ( height, width ) of the game field
    private var dimensions = intArrayOf()

    //    for the drawing of the field
    private val widthRelativeToScreen = 0.84
    private val heightRelativeToScreen = 1.092
    private var tileSize = 0.0
    private var fontSize = 0.0
    private var margin = 0

    private lateinit var linearLayout: LinearLayout
    private lateinit var linearLayoutBase: LinearLayout
    private lateinit var textView: TextView
    private val params = LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT,
        1.0f
    )

    //    for gestures
    private lateinit var gestureDetector: GestureDetector
    private val touchListener: View.OnTouchListener by lazy {
        View.OnTouchListener { view, event ->
            view.performClick()
            gestureDetector.onTouchEvent(event)
        }
    }

//    **********************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dimensions = arguments?.getIntArray(MainActivity.DIMENSIONS) ?: intArrayOf(4, 4)
        viewModel = ViewModelProvider(
            this,
            ModelFactory(requireActivity().application, dimensions)
        )[ViewModel2024::class.java]

        gameField =
            List(dimensions[0]) { MutableList(dimensions[1]) { Tile(requireContext()) } } // initializing the playing field with zeros
        viewModel.liveDataField.observe(
            this,
            fieldObserver
        ) // installing an observer for the playing field

        val displaySize =
            Resources.getSystem().displayMetrics.widthPixels // width of display on pixels
        val widthOfFrame = displaySize * widthRelativeToScreen
        val heightOfFrame = displaySize * heightRelativeToScreen

        tileSize =
            if ((dimensions[0].toDouble() / dimensions[1].toDouble()) > 1.3) // width of one Tile
                (heightOfFrame * 20) / (dimensions[0] * 22)
            else (widthOfFrame * 20) / (dimensions[1] * 22)

        fontSize = tileSize / 6
        margin = if (tileSize >= 40) (tileSize / 40).toInt() else 4 // margin between Tiles
    }

//    **********************************************************************************************

    @SuppressLint("ClickableViewAccessibility") // FrameLayout.performClick()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGameFieldBinding.inflate(inflater)

        gestureDetector = GestureDetector(requireContext(), GestureListener(this))
        setFieldView()
        binding.restartButton.setOnClickListener { reset() }
        binding.frameGameField.setOnTouchListener(touchListener)
        return binding.root
    }

//    **********************************************************************************************

    //    overrides methods
    override fun onStart() {
        super.onStart()
        viewModel.liveDataWinner.observe(this, isWinnerObserver)
        viewModel.liveDataLost.observe(this, isLostObserver)
        viewModel.liveDataScore.observe(this, scoreObserver)
        binding.homeButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_gameFieldFragment_to_startFragment)
        }
    }

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


    //    OBSERVERS OF LIVEDATA

    private val fieldObserver: Observer<List<MutableList<Int>>> = Observer { field ->
        gameField.mapIndexed { i, list -> list.mapIndexed { j, tile -> tile.value = field[i][j] } }
        setFieldView()
        Log.d("MyLogs", "fieldObserver")
    }

    private val isWinnerObserver: Observer<Boolean> = Observer {
        isGameWon = it
        Log.d("MyLogs", "isGameWon = $isGameWon")
    }

    private val isLostObserver: Observer<Boolean> = Observer {
        isGameLost = it
        Log.d("MyLogs", "isGameLost = $isGameLost")
    }

    private val scoreObserver: Observer<Int> = Observer {
        score = it
        binding.score.text = getString(R.string.score, score)
        Log.d("MyLogs", "score = $score")
    }

//    **********************************************************************************************

    private fun reset() {
        viewModel.resetGame()
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
                textView = getTextView(gameField[i][j])
                linearLayout.addView(textView)
            }
            linearLayoutBase.addView(linearLayout)
        }
        binding.gameField.addView(linearLayoutBase)
    }


    private fun getTextView(tile: Tile): TextView = TextView(context).apply {
        if (tile.value != 0) {
            text = tile.value.toString()
        }
        gravity = Gravity.CENTER
        textSize = fontSize.toFloat()
        width = tileSize.toInt()
        height = tileSize.toInt()
        setTextColor(tile.getFontColor())
        background =
            ResourcesCompat.getDrawable(resources, R.drawable.tile, null).apply {
                this?.overrideColor(tile.getTileColor())
            }

        layoutParams = params.apply {
            requestLayout()
            setMargins(margin, margin, margin, margin)
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