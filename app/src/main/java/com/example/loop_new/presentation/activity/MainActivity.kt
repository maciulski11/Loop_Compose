package com.example.loop_new.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.loop_new.DependencyProvider
import com.example.loop_new.data.firebase.FirebaseService
import com.example.loop_new.data.firebase.GoogleAuthService
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.example.loop_new.presentation.navigation.NavigationScreens
import com.example.loop_new.ui.theme.Loop_NewTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val interfaceFirebaseService: InterfaceFirebaseService =
        FirebaseService(DependencyProvider().firebaseFirestore)

    /**
     * Definition of the `googleAuthService` variable with lazy initialization.
     * The `GoogleAuthService` object is only created when it is first needed,
     * which improves application performance, especially at startup.
     *
     * GoogleAuthService` is used to handle the Google authentication process in the application
     *
     * @param context the application context is used inside `GoogleAuthService` for operations
     * @param oneTapClient an instance of `SignInClient` from Google, used to manage the process
     *                     Google One Tap logins.
     */
    private val googleAuthService by lazy {
        GoogleAuthService(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            interfaceFirebaseService.addFlashcardsToRepeatSection()
        }

        setContent {
            Loop_NewTheme {
                NavigationScreens(googleAuthService)
            }
        }
    }
}