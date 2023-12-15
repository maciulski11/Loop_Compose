package com.example.loop_new.domain.model.firebase.google

import com.example.loop_new.domain.model.firebase.User

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)