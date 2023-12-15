package com.example.loop_new.domain.services

import android.content.Intent
import android.content.IntentSender
import com.example.loop_new.domain.model.firebase.User
import com.example.loop_new.domain.model.firebase.google.SignInResult

interface GoogleAuthService {

    suspend fun signIn(): IntentSender?

    suspend fun signInWithIntent(intent: Intent): SignInResult

    suspend fun signOut()

}