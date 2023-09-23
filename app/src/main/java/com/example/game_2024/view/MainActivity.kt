package com.example.game_2024.view

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.game_2024.R
import com.example.game_2024.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    var mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState != null) {
            musicOn = savedInstanceState.getBoolean(MUSIC_STATE)
        }
        player = getPlayer(this, R.raw.sound_butterfly)
        player!!.startStopMusic(this, musicOn)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(MUSIC_STATE, musicOn)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    companion object {
        const val DIMENSIONS = "DIMENSIONS"
        private const val MUSIC_STATE = "MUSIC"

        private var player: MediaPlayer? = null
        private var musicOn = true

        fun getPlayer(context: Context, resId: Int): MediaPlayer {
            return if (player != null) {
                synchronized(this) {
                    if (player != null) player!!
                    else MediaPlayer.create(context, resId).apply { isLooping = true }
                }
            } else synchronized(this) {
                MediaPlayer.create(context, resId).apply { isLooping = true }
            }
        }

        fun musicSwitcher(activity: Activity, view: ConstraintLayout) {
            musicOn = !musicOn
            changeMusicIconState(activity, view)
            player?.startStopMusic(activity, musicOn)
        }

        fun changeMusicIconState(activity: Activity, view: ConstraintLayout) {
            if (musicOn) {
                ((view as ViewGroup).getChildAt(0) as ImageView).setImageDrawable(
                    ResourcesCompat.getDrawable(activity.resources, R.drawable.music_default, null)
                )
            } else ((view as ViewGroup).getChildAt(0) as ImageView).setImageDrawable(
                ResourcesCompat.getDrawable(activity.resources, R.drawable.no_music, null)
            )
        }
    }
}

private fun MediaPlayer.startStopMusic(activity: Activity, musicOn: Boolean) {
//    (activity as MainActivity).mainScope.launch {
        if (musicOn && !isPlaying) start()
        else if (!musicOn && isPlaying) pause()
//    }
}


