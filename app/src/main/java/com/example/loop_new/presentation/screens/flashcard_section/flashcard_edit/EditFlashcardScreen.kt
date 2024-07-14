package com.example.loop_new.presentation.screens.flashcard_section.flashcard_edit

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.loop_new.presentation.navigation.NavigationSupport

@Composable
fun EditFlashcardScreen(navController: NavController, flashcardId: Int) {

    // Support for custom return behavior
    BackHandler {
        // Where return
        navController.popBackStack()
    }
}