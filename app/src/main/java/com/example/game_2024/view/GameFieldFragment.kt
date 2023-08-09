package com.example.game_2024.view

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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

class GameFieldFragment : Fragment() {

    private lateinit var binding: FragmentGameFieldBinding

    private val observer: Observer<Int> = Observer {

    }

    private var isGameWon = false
    private var isGameLost = false

    private var dimensions = intArrayOf()
    private lateinit var viewModel: ViewModel2024
    private lateinit var gameField: List<List<Tile>>

    private var score = 0
    private var maxTile = 0

    private val widthRelativeToScreen = 0.84
    private val heightRelativeToScreen = 1.092
    private var tileSize = 0.0
    private var fontSize = 0.0
    private var margin = 0.0

    private lateinit var linearLayout: LinearLayout
    private lateinit var linearLayoutBase: LinearLayout
    private lateinit var textView: TextView
    private val params = LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT,
        1.0f
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dimensions = arguments?.getIntArray(MainActivity.DIMENSIONS) ?: intArrayOf(4, 4)
        viewModel = ViewModelProvider(
            this,
            ModelFactory(requireActivity().application, dimensions)
        )[ViewModel2024::class.java]

        setGameField()
        score = viewModel.getScore()
        maxTile = viewModel.getMaxTile()

        val displaySize = Resources.getSystem().displayMetrics.widthPixels
        val widthOfFrame = displaySize * widthRelativeToScreen
        val heightOfFrame = displaySize * heightRelativeToScreen

        tileSize = if ((dimensions[0].toDouble() / dimensions[1].toDouble()) > 1.3)
            (heightOfFrame * 20) / (dimensions[0] * 22)
        else (widthOfFrame * 20) / (dimensions[1] * 22)

        fontSize = tileSize / 6
        margin = if (tileSize >= 40) tileSize / 40 else 4.0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFieldBinding.inflate(inflater)
        setFieldView()
        binding.restartButton.setOnClickListener {
            viewModel.resetGame()
            setGameField()
            setFieldView()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.liveData.observe(this, observer)
        binding.homeButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_gameFieldFragment_to_startFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.liveData.removeObserver(observer)
    }

    private fun setFieldView() {

        binding.gameField.removeAllViews()

        val heightOfField = dimensions.component1()
        val widthOfField = dimensions.component2()

        linearLayoutBase = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            weightSum = heightOfField.toFloat()
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
                textView = TextView(context).apply {
                    text = gameField[i][j].value.toString()
                    gravity = Gravity.CENTER
                    textSize = fontSize.toFloat()
                    width = tileSize.toInt()
                    height = tileSize.toInt()
                    setTextColor(gameField[i][j].getFontColor())
                    background = ResourcesCompat.getDrawable(resources, R.drawable.tile, null).apply {
                        this?.overrideColor(gameField[i][j].getTileColor())
                    }

                    layoutParams = params.apply {
                        requestLayout()
                        setMargins(
                            margin.toInt(),
                            margin.toInt(),
                            margin.toInt(),
                            margin.toInt()
                        )
                    }
                }
                linearLayout.addView(textView)
            }
            linearLayoutBase.addView(linearLayout)
        }
        binding.gameField.addView(linearLayoutBase)
    }

    private fun setGameField() {
        gameField = viewModel.getGameField().map { list -> list.map { Tile(it, requireContext()) } }
    }

    private fun Drawable.overrideColor(@ColorInt colorInt: Int) {
        mutate()
        when (this) {
            is GradientDrawable -> setColor(colorInt)
            is ShapeDrawable -> paint.color = colorInt
            is ColorDrawable -> color = colorInt
        }
    }
}