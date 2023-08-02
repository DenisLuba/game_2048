package com.example.game_2024

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.game_2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var model: Model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
//        model = ViewModelProvider(this).get(Model::class.java)
        model = ViewModelProvider(this)[Model::class.java]
    }

    override fun onStart() {
        super.onStart()
        model.liveData.observe(this) {
            // TODO
        }
    }
}