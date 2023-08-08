package com.example.game_2024.view

import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game_2024.view_model.ModelFactory
import com.example.game_2024.R
import com.example.game_2024.Tile
import com.example.game_2024.view_model.ViewModel2024
import com.example.game_2024.databinding.FragmentGameFieldBinding

class GameFieldFragment : Fragment() {

    private lateinit var binding: FragmentGameFieldBinding

    private val observer: Observer<Int> = Observer {

    }

    private var isGameWon = false
    private var isGameLost = false

    private var fieldSize = 4
    private lateinit var viewModel: ViewModel2024
    private lateinit var gameField: List<List<Tile>>
    private var score = 0
    private var maxTile = 0
    private var tileSize = 0
    private var fontSize = 0
    private var margin = 0

    private lateinit var linearLayout: LinearLayout
    private lateinit var textView: TextView
    private val params = LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT,
        1.0f
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fieldSize = arguments?.getInt(MainActivity.FIELD_SIZE) ?: 4
        viewModel = ViewModelProvider(
            this,
            ModelFactory(requireActivity().application, fieldSize)
        )[ViewModel2024::class.java]

        gameField = viewModel.getGameField().map { list -> list.map { Tile(it) } }
        score = viewModel.getScore()
        maxTile = viewModel.getMaxTile()
        tileSize = (((Resources.getSystem().displayMetrics.widthPixels * 0.84).toInt() / fieldSize) / 4) * 4
        fontSize = tileSize / 6
        margin = if (tileSize >= 40) (fontSize / 40) * 4 else 4
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFieldBinding.inflate(inflater)
        setField()
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

    private fun setField() {
        for (i in 0 until fieldSize) {
            linearLayout = LinearLayout(context).apply {
                weightSum = fieldSize.toFloat()
                layoutParams = params
            }
            for (j in 0 until fieldSize) {
                textView = TextView(context).apply {
                    text = gameField[i][j].value.toString()
                    gravity = Gravity.CENTER
                    textSize = fontSize.toFloat()
                    setTextColor(gameField[i][j].getFontColor())
                    setBackgroundColor(gameField[i][j].getTileColor())
                    background = ResourcesCompat.getDrawable(resources, R.drawable.tile, null)
                    layoutParams = params.apply {
                        setMargins(2 * margin, margin, 2 * margin, margin)
                    }
                }
                linearLayout.addView(textView)
            }
            binding.gameField.addView(linearLayout)
        }
    }
}