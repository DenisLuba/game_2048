package com.example.game_2024

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.game_2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    private var width = 4
    private var height = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment
        navController = navHostFragment.navController

//        model = ViewModelProvider(this).get(Model::class.java)

    }

    override fun onStart() {
        super.onStart()

    }

    fun getWidth(): Int = width
    fun getHeight(): Int = height

    override fun onStop() {
        super.onStop()

    }

    companion object{
        const val DIMENSIONS = "DIMENSIONS"
    }
}