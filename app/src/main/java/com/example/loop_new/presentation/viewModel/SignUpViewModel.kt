package com.example.loop_new.presentation.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.room.LoopDatabase
//import com.example.loop_new.room.LoopDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application): ViewModel() {


    private val repository = LoopDatabase.getInstance(application).loopDao()

    fun insert(box: Box) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBox(box)
        }
    }
}
