package com.example.loop_new.presentation.screens.flashcard_section.repeat

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RepeatScreen(navController: NavController, viewModel: RepeatViewModel) {

    val currentFlashcard by viewModel.currentRepeatSection.collectAsState()
    val flashcardList = viewModel.repeatSectionList.value

    // Dodajmy sprawdzenie, czy currentFlashcard nie jest nullem
    val indexOfFlashcard = if (currentFlashcard != null) {
        flashcardList.indexOf(currentFlashcard)
    } else {
        -1
    }

    if (indexOfFlashcard != -1) {
        val flashcardId = flashcardList[indexOfFlashcard].flashcardId ?: -1

        currentFlashcard?.let {
            Repeat(
                navController = navController,
                repeatList = flashcardList,
                progressText = viewModel.progressText,
                progress = viewModel.progress,
                currentFlashcard = it,
                { audioUrl ->
                    viewModel.playAudioFromUrl(audioUrl)
                },
                {
                    viewModel.updateFlashcardToKnow(flashcardId)
                    viewModel.moveToNextRepeatSection(navController)
                },
                {
                    viewModel.updateFlashcardToSomewhatKnow(flashcardId)
                    viewModel.moveToNextRepeatSection(navController)
                },
                {
                    viewModel.updateFlashcardToDoNotKnow(flashcardId)
                    viewModel.moveToNextRepeatSection(navController)
                }
            )
        }
    }
}
