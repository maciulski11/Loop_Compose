package com.example.loop_new.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loop_new.DependencyProvider
import com.example.loop_new.presentation.screens.add_flashcard.AddFlashcardScreen
import com.example.loop_new.presentation.screens.add_flashcard.AddFlashcardViewModel
import com.example.loop_new.presentation.screens.box.BoxScreen
import com.example.loop_new.presentation.screens.box.BoxViewModel
import com.example.loop_new.presentation.screens.lesson.LessonScreen
import com.example.loop_new.presentation.screens.lesson.LessonViewModel
import com.example.loop_new.presentation.screens.main.MainScreen
import com.example.loop_new.presentation.screens.main.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

object NavigationSupport {
    const val MainScreen = "main_screen"
    const val BoxScreen = "box_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NavigationScreens() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationSupport.MainScreen
    ) {

        composable(NavigationSupport.MainScreen) {
            val viewModel = remember { MainViewModel(DependencyProvider().firebaseServices) }

            MainScreen(navController, viewModel)
        }

        composable(
            "${NavigationSupport.BoxScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val viewModel = remember { BoxViewModel(DependencyProvider().firebaseServices, boxUid) }

            BoxScreen(navController, boxUid, viewModel)
        }

        composable(
            "${NavigationSupport.AddFlashcardScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val viewModel =
                remember {
                    AddFlashcardViewModel(
                        DependencyProvider().firebaseServices,
                        DependencyProvider().translateService,
                        DependencyProvider().dictionaryService
                    )
                }

            AddFlashcardScreen(navController, boxUid, viewModel)
        }

        composable("${NavigationSupport.LessonScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val viewModel = remember { LessonViewModel(DependencyProvider().firebaseServices, boxUid) }

            LessonScreen(navController, viewModel, boxUid)
        }
    }
}