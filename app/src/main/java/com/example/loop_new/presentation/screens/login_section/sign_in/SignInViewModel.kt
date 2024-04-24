package com.example.loop_new.presentation.screens.login_section.sign_in

import androidx.lifecycle.ViewModel
import com.example.loop_new.domain.model.firebase.google.SignInResult
import com.example.loop_new.domain.model.firebase.google.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel : ViewModel() {

    //  Mutable state flow to hold the current sign-in state
    private val _state = MutableStateFlow(SignInState())

    // Read-only version of the sign-in state. UI components can observe this state
    // to react to changes in the sign-in process
    val state = _state.asStateFlow()

    /**
     * Updates the sign-in state based on the result of the sign-in attempt.
     *
     * This function takes a SignInResult object (which contains information about the success
     * or failure of a sign-in attempt) and updates the state flow accordingly.
     *
     * @param result The result of a sign-in attempt.
     */
    fun onSignInResult(result: SignInResult) {

        // Updating the state with the result of the sign-in attempt
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage,
            )
        }
    }

    /**
     * Resets the sign-in state to its initial state.
     *
     * This can be used to clear the current state, for example, when starting a new sign-in attempt
     * or when navigating away from the sign-in screen.
     */
    fun resetState() {
        // Resetting the state to its initial, default values.
        _state.update { SignInState() }
    }
}