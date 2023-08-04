package com.example.game_2024

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.game_2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: Model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationHost) as NavHostFragment
        navController = navHostFragment.navController

//        model = ViewModelProvider(this).get(Model::class.java)
        model = ViewModelProvider(this, ModelFactory(application, ))[Model::class.java]
    }

    override fun onStart() {
        super.onStart()
        model.liveData.observe(this) {

        }
    }
}