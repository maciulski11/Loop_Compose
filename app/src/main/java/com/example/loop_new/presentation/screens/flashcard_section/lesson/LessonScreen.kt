package com.example.loop_new.presentation.screens.flashcard_section.lesson

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.presentation.screens.flashcard_section.LessonRepeatScreen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LessonScreen(navController: NavController, viewModel: LessonViewModel) {

    val currentFlashcard = viewModel.currentFlashcard.value
    val flashcardList = viewModel.flashcardList.value.orEmpty() // Jeśli lista jest null, zwróć pustą listę
    val indexOfFlashcard = flashcardList.indexOf(currentFlashcard)

    currentFlashcard?.let {
        LessonRepeatScreen(
            navController = navController,
            flashcardList = flashcardList,
            progressText = viewModel.progressText,
            progress = viewModel.progress,
            currentFlashcard = it,
            { audioUrl ->
                viewModel.playAudioFromUrl(audioUrl)
            },
            {   // onKnowFlashcard
                viewModel.updateFlashcardToKnow(
                    flashcardList[indexOfFlashcard].idFlashcard,
                    KnowledgeLevel.KNOW.value
                )
                viewModel.moveToNextFlashcard(navController)

//                viewModel.addLessonStatsToFirestore(flashcardList[indexOfFlashcard].uid.toString(), KnowledgeLevel.KNOW.value)

//                viewModel.deleteFlashcardFromRepeatSection(flashcardList[indexOfFlashcard].uid.toString())
            },
            {   // onSomewhatKnowFlashcard
                viewModel.updateFlashcardToSomewhatKnow(
                    flashcardList[indexOfFlashcard].idFlashcard,
                    KnowledgeLevel.SOMEWHAT_KNOW.value
                )
                viewModel.moveToNextFlashcard(navController)

//                viewModel.addLessonStatsToFirestore(flashcardList[indexOfFlashcard].uid.toString(), KnowledgeLevel.SOMEWHAT_KNOW.value)

//                viewModel.deleteFlashcardFromRepeatSection(flashcardList[indexOfFlashcard].uid.toString())
            },
            {   // onDoNotKnowFlashcard
                viewModel.updateFlashcardToDoNotKnow(
                    flashcardList[indexOfFlashcard].idFlashcard,
                    KnowledgeLevel.DO_NOT_KNOW.value
                )
                viewModel.moveToNextFlashcard(navController)

//                viewModel.addLessonStatsToFirestore(flashcardList[indexOfFlashcard].uid.toString(), KnowledgeLevel.DO_NOT_KNOW.value)

//                viewModel.deleteFlashcardFromRepeatSection(flashcardList[indexOfFlashcard].uid.toString())
            }
        )
    }
}