package com.example.game_2024

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ViewModel2024(application: Application): AndroidViewModel(application) {
    //class ViewModel2024: ViewModel() {

    val liveData = MutableLiveData<Int>() // may be not Int
}