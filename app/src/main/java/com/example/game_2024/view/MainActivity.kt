package com.example.game_2024.view

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.game_2024.R
import com.example.game_2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        player = MediaPlayer.create(this, R.raw.sound_butterfly)
        player.isLooping = true

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    companion object {
        const val DIMENSIONS = "DIMENSIONS"

        var musicOn = true
        private lateinit var player: MediaPlayer

        fun startStopMusic() {
            if (musicOn) {
                player.start()
            } else {
                player.pause()
            }
        }
    }
}