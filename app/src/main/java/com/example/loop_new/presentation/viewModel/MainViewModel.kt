package com.example.loop_new.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.services.GoogleAuthService
import com.example.loop_new.domain.services.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val service: Service,
    private val googleAuthService: GoogleAuthService,
) : ViewModel() {

    private val _isSignedOut = MutableStateFlow(false)
    val isSignedOut = _isSignedOut.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            googleAuthService.signOut()
            _isSignedOut.value = true
        }
    }

    fun playAudioFromUrl(audioUrl: String) {
        viewModelScope.launch {
            service.playAudioFromUrl(audioUrl)
        }
    }
}