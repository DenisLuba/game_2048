package com.example.game_2024.view.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.TypedValue
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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game_2024.view_model.ModelFactory
import com.example.game_2024.R
import com.example.game_2024.view_model.ViewModel2024
import com.example.game_2024.databinding.FragmentGameFieldBinding
import com.example.game_2024.view.ButtonsAnimation
import com.example.game_2024.view.MainActivity
import com.example.game_2024.view.Tile
import com.example.game_2024.view.dialogs.ExitDialog
import com.example.game_2024.view.dialogs.GameOverDialog
import com.example.game_2024.view.dialogs.ResetDialog
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
    private var verticalRelativeToScreen: Float = .0f
    private var horizontalRelativeToScreen: Float = .0f
    private var tileSize = 0.0f
    private var fontSize = 0.0f
    private var margin = 0
    private var portrait = true
    private var landscape = false

    private lateinit var linearLayout: LinearLayout
    private lateinit var linearLayoutBase: LinearLayout
    private val params = LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT,
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
        val verticalTiles = dimensions[0]
        val horizontalTiles = dimensions[1]

        val outVerticalValue = TypedValue()
        val outHorizontalValue = TypedValue()
        resources.getValue(R.dimen.vertical_relative_to_screen, outVerticalValue, true)
        verticalRelativeToScreen = outVerticalValue.float
        resources.getValue(R.dimen.horizontal_relative_to_screen, outHorizontalValue, true)
        horizontalRelativeToScreen = outHorizontalValue.float

        portrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val gameFieldWidth = if (portrait)
            Resources.getSystem().displayMetrics.widthPixels * horizontalRelativeToScreen
        else Resources.getSystem().displayMetrics.heightPixels * horizontalRelativeToScreen

        val gameFieldHeight = if (portrait)
            Resources.getSystem().displayMetrics.heightPixels * verticalRelativeToScreen
        else Resources.getSystem().displayMetrics.widthPixels * verticalRelativeToScreen

        tileSize = if (landscape && gameFieldWidth / verticalTiles <= gameFieldHeight / horizontalTiles)
            (gameFieldWidth * 8) / (9 * verticalTiles)
        else if (landscape && gameFieldWidth / verticalTiles > gameFieldHeight / horizontalTiles)
            (gameFieldHeight * 8) / (9 * horizontalTiles)
        else if (portrait && gameFieldHeight / verticalTiles <= gameFieldWidth / horizontalTiles)
            (gameFieldHeight * 8) / (9 * verticalTiles)
        else (gameFieldWidth * 8) / (9 * horizontalTiles)

        margin = (tileSize / 40).toInt() // margin between Tiles

        fontSize = (65 /
                    if (dimensions.component1() > dimensions.component2()) dimensions.component1()
                    else dimensions.component2()
                    ).toFloat()

        viewModel = ViewModelProvider(
            this,
            ModelFactory(requireActivity().application, args)
        )[ViewModel2024::class.java]

        gameField = List(dimensions.component1()) {
            MutableList(dimensions.component2()) { Tile(requireContext()) }
        } // initializing the playing field with zeros

        setupGameOverListener(childFragmentManager, this) { reset() }
    }

//    **********************************************************************************************

    @SuppressLint("ClickableViewAccessibility") // FrameLayout.performClick()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        with(viewModel) {
            liveDataField.observe(viewLifecycleOwner, fieldObserver)
            liveDataScore.observe(viewLifecycleOwner, scoreObserver)
            liveDataMaxScore.observe(viewLifecycleOwner, maxScoreObserver)
            liveDataWinner.observe(viewLifecycleOwner, isWinnerObserver)
            liveDataLost.observe(viewLifecycleOwner, isLostObserver)
        }

        textViews = Array(dimensions.component1()) {
            Array(dimensions.component2()) {
                TextView(requireContext()).apply {
                    gravity = Gravity.CENTER
                    textSize = fontSize
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

            ButtonsAnimation.start(requireActivity(), homeButton, true)
            ButtonsAnimation.start(requireActivity(), undoButton, false)
            ButtonsAnimation.start(requireActivity(), restartButton, false)

            exitButton.setOnClickListener {
                ExitDialog.show(childFragmentManager)
            }

            undoButton.setOnClickListener {

                ButtonsAnimation.animationShift(
                    requireActivity(),
                    homeButton,
                    undoButton,
                    restartButton,
                    pit,
                    ButtonsAnimation.undo
                )

                rollback()
            }
            frameGameField.setOnTouchListener(touchListener)
            homeButton.setOnClickListener {

                ButtonsAnimation.animationShift(
                    requireActivity(),
                    homeButton,
                    undoButton,
                    restartButton,
                    pit,
                    ButtonsAnimation.home
                )

                (activity as MainActivity).navController.navigate(R.id.action_gameFieldFragment_to_startFragment)
            }
            restartButton.setOnClickListener {

                ButtonsAnimation.animationShift(
                    requireActivity(),
                    homeButton,
                    undoButton,
                    restartButton,
                    pit,
                    ButtonsAnimation.reset
                )

                ResetDialog.show(childFragmentManager)
            }
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
        setTextViews()
    }

    private val isWinnerObserver: Observer<Boolean> = Observer {
        isGameWon = it
        if (isGameWon) {
            gameOver(WIN)
        }
    }

    private val isLostObserver: Observer<Boolean> = Observer {
        isGameLost = it
        if (isGameLost) gameOver(GAME_OVER)
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

    private fun gameOver(text: String) {
        GameOverDialog.showGameOver(childFragmentManager, text)
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

        val verticalTiles = dimensions.component1()
        val horizontalTiles = dimensions.component2()

        linearLayoutBase = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            weightSum = verticalTiles.toFloat()
            foregroundGravity = Gravity.CENTER
            background = ResourcesCompat.getDrawable(resources, R.drawable.game_field, null)

            layoutParams = params
        }

        for (i in 0 until verticalTiles) {
            linearLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                weightSum = horizontalTiles.toFloat()
                layoutParams = params.apply {
                    gravity = Gravity.CENTER
                }
            }
            for (j in 0 until horizontalTiles) {
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

                    typeface = ResourcesCompat.getFont(requireContext(), R.font.dela_gothic_one)

                    val tileColor: Int = tile.getTileColor()
                    background =
                        ResourcesCompat.getDrawable(resources, R.drawable.tile, null).apply {

                            ((this as LayerDrawable).findDrawableByLayerId(R.id.tile) as GradientDrawable).apply {
                                setColor(tileColor)
                                val size =
                                    if (dimensions[0] > dimensions[1]) dimensions[0] else dimensions[1]
                                if (size > 7) cornerRadius = 8f
                            }
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
        const val GAME_OVER = "Game over..."
        const val WIN = "You win!!!"

        const val REQUEST_KEY = "REQUEST_KEY"
        const val RESULT = "RESULT"

        fun setupGameOverListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: () -> Unit
        ) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner) { _, bundle ->
                when (bundle.getString(RESULT)) {
                    GameOverDialog.WIN_KEY -> listener.invoke()
//                  GameOverDialog.GAME_OVER_KEY -> nothing
                    ResetDialog.YES -> listener.invoke()
//                  ResetDialog.NO -> nothing
                }
            }
        }
    }
}