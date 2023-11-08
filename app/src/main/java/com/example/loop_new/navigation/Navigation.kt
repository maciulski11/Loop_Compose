package com.example.loop_new.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loop_new.data.api.TranslateService
import com.example.loop_new.presentation.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.add_flashcard.AddFlashcardViewModel
import com.example.loop_new.presentation.box.BoxScreen
import com.example.loop_new.presentation.box.BoxViewModel
import com.example.loop_new.presentation.lesson.LessonScreen
import com.example.loop_new.presentation.main.MainScreen
import com.example.loop_new.presentation.main.MainViewModel
import com.example.loop_new.data.api.DictionaryService
import com.example.loop_new.data.firebase.FirebaseServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi

object Navigation {
    const val MainScreen = "main_screen"
    const val BoxScreen = "box_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NavigationScreens() {
    val navController = rememberNavController()

    val firebaseFirestore = FirebaseFirestore.getInstance()

    NavHost(
        navController = navController,
        startDestination = Navigation.MainScreen
    ) {

        composable(Navigation.MainScreen) {
            val mainViewModel = MainViewModel(FirebaseServices(firebaseFirestore))
            MainScreen(navController, mainViewModel)
        }

        composable(
            "${Navigation.BoxScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val boxViewModel = BoxViewModel(FirebaseServices(firebaseFirestore), boxUid = boxUid)

            BoxScreen(navController, boxUid, boxViewModel)
        }

        composable(
            "${Navigation.AddFlashcardScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val addFlashcardViewModel = AddFlashcardViewModel(FirebaseServices(firebaseFirestore), TranslateService(), DictionaryService())

            AddFlashcardScreen(navController, addFlashcardViewModel, boxUid)
        }

        composable(Navigation.LessonScreen) {
            LessonScreen()
        }
    }
}