package com.example.loop_new.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.loop_new.DependencyProvider
import com.example.loop_new.data.firebase.FirebaseServices
import com.example.loop_new.domain.services.InterfaceFirebaseService
import com.example.loop_new.presentation.navigation.NavigationScreens
import com.example.loop_new.ui.theme.Loop_NewTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val interfaceFirebaseServices: InterfaceFirebaseService = FirebaseServices(DependencyProvider().firebaseFirestore)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            interfaceFirebaseServices.fetchRepeatFlashcards()
        }

        setContent {
            Loop_NewTheme {
                NavigationScreens()
            }
        }
    }
}