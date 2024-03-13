package com.example.loop_new.data.firebase

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.User
import com.example.loop_new.domain.model.firebase.google.SignInResult
import com.example.loop_new.domain.services.GoogleAuthService
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

/**
 * Class for handling Google authentication.
 *
 * This class encapsulates the logic for Google Sign-In authentication, including
 * signing in, signing out, and retrieving the currently signed-in user's information.
 * It utilizes Google's One Tap Sign-In API and Firebase Authentication.
 *
 * @param oneTapClient An instance of SignInClient provided by Google's Sign-In API.
 *                     Used for initiating sign-in and sign-out processes.
 */
class GoogleAuthService(
    private val context: Context,
    private val oneTapClient: SignInClient,
) : GoogleAuthService {
    private val auth = Firebase.auth

    /**
     * Initiates the Google Sign-In process.
     *
     * This is a suspending function that can be called within a coroutine context. It attempts
     * to initiate a sign-in process using the Google One Tap Sign-In API. In case of an error
     * during the sign-in process, it handles exceptions and returns null. It utilizes the
     * buildSignInRequest` function to create the necessary sign-in request.
     *
     * @return An IntentSender for the sign-in activity, or null if an error occurs.
     */
    override suspend fun signIn(): IntentSender? {
        // Try to start the Google Sign-In process and handle exceptions
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace() // Log the exception for debugging purposes.
            if (e is CancellationException) throw e
            null
        }
        // Extracting the IntentSender if the result is non-null
        return result?.pendingIntent?.intentSender
    }

    /**
     * Builds the Google Sign-In request.
     *
     * This function constructs a BeginSignInRequest object required by the Google Sign-In API.
     * It sets up the necessary request options, including the server client ID, and specifies
     * whether automatic selection is enabled.
     *
     * @return A BeginSignInRequest instance configured for the sign-in process.
     */
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            // Configuring the ID token request options
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Allows the user to choose an account.
                    .setFilterByAuthorizedAccounts(false)
                    // The server client ID verify correctly of the ID token received
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            // Enabling auto-selection to streamline the user experience by reducing manual account selection
            .setAutoSelectEnabled(true)
            .build()
    }

    /**
     * Completes the Google Sign-In process using the provided Intent.
     *
     * This suspending function extracts the sign-in credentials from the Intent received
     * from the Google Sign-In process and uses them to authenticate with Firebase.
     *
     * @param intent The Intent received from the Google Sign-In process.
     * @return A SignInResult object containing the user information or an error message.
     */
    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        // Extracting Google Sign-In credentials from the intent
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        // ID token from the Google Sign-In credentials
        val googleIdToken = credential.googleIdToken
        // Creating Firebase credentials using the Google ID token
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        // Attempt to sign in with the Firebase credentials
        return try {
            // This coroutine is suspended until the Firebase authentication process is complete
            val user = auth.signInWithCredential(googleCredentials).await().user
            // If successful, return a SignInResult with user data
            SignInResult(
                data = user?.run {
                    User(
                        email = email,
                        uid = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            // In case of an error, return a SignInResult with the error message
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    /**
     * Signs out the currently signed-in Google user.
     *
     * This is a suspending function that first signs out of the Google One Tap Sign-In API,
     * and then signs out from Firebase Authentication. The coroutine is suspended until
     * Google's sign-out process is completed.
     */
    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await() // Sign out from Google One Tap Sign-In
            auth.signOut()                 // Sign out from Firebase Authentication
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }
}