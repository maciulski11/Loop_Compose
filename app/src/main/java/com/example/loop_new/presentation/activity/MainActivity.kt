package com.example.loop_new.presentation.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.loop_new.di.DependencyProvider
import com.example.loop_new.data.firebase.GoogleAuthService
import com.example.loop_new.presentation.navigation.NavigationScreens
import com.example.loop_new.ui.theme.Loop_NewTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var dependencyProvider: DependencyProvider

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

    private val firebaseService by lazy {
        dependencyProvider.firebaseService
    }

    private val translateService by lazy {
        dependencyProvider.translateService
    }

    private val dictionaryService by lazy {
        dependencyProvider.dictionaryService
    }

    private val roomService by lazy {
        dependencyProvider.roomService
    }

    private val service by lazy {
        dependencyProvider.service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dependencyProvider = DependencyProvider(application)

        lifecycleScope.launch(Dispatchers.IO) {
            firebaseService.addFlashcardsToRepeatSection()
        }

        setContent {
            Loop_NewTheme {
                NavigationScreens(
                    googleAuthService = googleAuthService,
                    firebaseService = firebaseService,
                    translateService = translateService,
                    dictionaryService = dictionaryService,
                    service = service,
                    roomService = roomService
                )
            }
        }
    }
}