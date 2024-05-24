package com.example.loop_new.presentation.screens.flashcard_section.lesson

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LessonScreen(navController: NavController, viewModel: LessonViewModel) {

    val currentFlashcard by viewModel.currentFlashcard.collectAsState(initial = null)
    val flashcardList = viewModel.flashcardList.value.orEmpty() // Jeśli lista jest null, zwróć pustą listę
    val indexOfFlashcard = flashcardList.indexOf(currentFlashcard)

    currentFlashcard?.let {
        Lesson(
            navController = navController,
            flashcardList = flashcardList,
            progressText = viewModel.progressText,
            progress = viewModel.progress,
            currentFlashcard = it,
            { audioUrl ->
                viewModel.playAudioFromUrl(audioUrl)
            },
            {
                // onKnowFlashcard
                viewModel.updateFlashcardToKnow(flashcardList[indexOfFlashcard].idFlashcard)
                viewModel.moveToNextFlashcard(navController)

//                viewModel.addLessonStatsToFirestore(flashcardList[indexOfFlashcard].uid.toString(), KnowledgeLevel.KNOW.value)
            },
            {
                // onSomewhatKnowFlashcard
                viewModel.updateFlashcardToSomewhatKnow(flashcardList[indexOfFlashcard].idFlashcard)
                viewModel.moveToNextFlashcard(navController)

//                viewModel.addLessonStatsToFirestore(flashcardList[indexOfFlashcard].uid.toString(), KnowledgeLevel.SOMEWHAT_KNOW.value)
            },
            {
                // onDoNotKnowFlashcard
                viewModel.updateFlashcardToDoNotKnow(flashcardList[indexOfFlashcard].idFlashcard)
                viewModel.moveToNextFlashcard(navController)

//                viewModel.addLessonStatsToFirestore(flashcardList[indexOfFlashcard].uid.toString(), KnowledgeLevel.DO_NOT_KNOW.value)

            }
        )
    }
}