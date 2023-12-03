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
import com.example.loop_new.presentation.screens.flashcard.FlashcardScreen
import com.example.loop_new.presentation.screens.flashcard.FlashcardViewModel
import com.example.loop_new.presentation.screens.lesson.LessonScreen
import com.example.loop_new.presentation.screens.lesson.LessonViewModel
import com.example.loop_new.presentation.screens.box.BoxScreen
import com.example.loop_new.presentation.screens.box.BoxViewModel
import com.example.loop_new.presentation.screens.repeat.RepeatScreen
import com.example.loop_new.presentation.screens.repeat.RepeatViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

object NavigationSupport {
    const val BoxScreen = "box_screen"
    const val FlashcardScreen = "flashcard_screen"
    const val AddFlashcardScreen = "add_flashcard_screen"
    const val LessonScreen = "lesson_screen"
    const val RepeatScreen = "repeat_screen"
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NavigationScreens() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationSupport.BoxScreen
    ) {

        composable(NavigationSupport.BoxScreen) {
            val viewModel = remember { BoxViewModel(DependencyProvider().firebaseServices) }

            BoxScreen(navController, viewModel)
        }

        composable(
            "${NavigationSupport.FlashcardScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val viewModel = remember {
                FlashcardViewModel(
                    DependencyProvider().firebaseServices,
                    DependencyProvider().mainViewModel,
                    boxUid
                )
            }
            FlashcardScreen(navController, boxUid, viewModel)
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

        composable(
            "${NavigationSupport.LessonScreen}/{boxUid}",
            arguments = listOf(navArgument("boxUid") { type = NavType.StringType })
        ) { backStackEntry ->
            val boxUid = backStackEntry.arguments?.getString("boxUid") ?: ""
            val viewModel = remember {
                LessonViewModel(
                    DependencyProvider().firebaseServices,
                    DependencyProvider().mainViewModel,
                    boxUid
                )
            }
            LessonScreen(navController, viewModel, boxUid)
        }

        composable(NavigationSupport.RepeatScreen) {
            val viewModel = remember {
                RepeatViewModel(
                    DependencyProvider().firebaseServices,
                    DependencyProvider().mainViewModel
                )
            }
            RepeatScreen(navController, viewModel)
        }
    }
}