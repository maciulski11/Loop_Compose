package com.example.loop_new.domain.model.firebase.google

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)