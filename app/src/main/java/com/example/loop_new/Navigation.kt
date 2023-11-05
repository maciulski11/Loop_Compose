package com.example.loop_new

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loop_new.presentation.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.box.BoxScreen
import com.example.loop_new.presentation.lesson.LessonScreen
import com.example.loop_new.presentation.main.MainScreen
import com.example.loop_new.presentation.main.MainViewModel
import com.example.loop_new.repository.FirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore

object Navigation {
    const val MainScreen = "main_screen"
    const val BoxScreen = "box_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
}

@Composable
fun NavigationScreens() {
    val navController = rememberNavController()

    val firebaseFirestore = FirebaseFirestore.getInstance()
    val mainViewModel = MainViewModel(FirebaseRepository(firebaseFirestore))

    NavHost(
        navController = navController,
        startDestination = Navigation.MainScreen
    ) {
        composable(Navigation.MainScreen) {
            MainScreen(navController, mainViewModel)
        }
        composable(
            "${Navigation.BoxScreen}/{x}",
            arguments = listOf(navArgument("x") { type = NavType.StringType })
        ) { backStackEntry ->
            val x = backStackEntry.arguments?.getString("x") ?: ""
            BoxScreen(navController, x)
        }
        composable(Navigation.AddFlashcardScreen) {
            AddFlashcardScreen()
        }
        composable(Navigation.LessonScreen) {
            LessonScreen()
        }
    }
}