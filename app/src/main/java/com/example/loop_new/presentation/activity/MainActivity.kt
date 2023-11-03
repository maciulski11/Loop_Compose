package com.example.loop_new.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.domain.model.Box
import com.example.loop_new.NavigationScreens
import com.example.loop_new.NavigationScreens.MainScreen
import com.example.loop_new.presentation.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.box.BoxScreen
import com.example.loop_new.presentation.lesson.LessonScreen
import com.example.loop_new.presentation.main.MainScreen
import com.example.loop_new.presentation.main.MainViewModel
import com.example.loop_new.repository.FirebaseRepository
import com.example.loop_new.ui.theme.Loop_NewTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseFirestore = FirebaseFirestore.getInstance()
        val mainViewModel = MainViewModel(FirebaseRepository(firebaseFirestore))

        setContent {
            Loop_NewTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MainScreen
                ) {
                    composable(MainScreen) {
                        MainScreen(navController, mainViewModel)
                    }
                    composable(NavigationScreens.BoxScreen) {
                        BoxScreen()
                    }
                    composable(NavigationScreens.AddFlashcardScreen) {
                        AddFlashcardScreen()
                    }
                    composable(NavigationScreens.LessonScreen) {
                        LessonScreen()
                    }
                }
            }
        }
    }
}