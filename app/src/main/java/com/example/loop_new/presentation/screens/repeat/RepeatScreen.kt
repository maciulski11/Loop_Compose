package com.example.loop_new.presentation.screens.repeat

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.loop_new.presentation.screens.LessonRepeatScreen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RepeatScreen(navController: NavController, viewModel: RepeatViewModel) {

    val currentFlashcard by viewModel.currentFlashcard.collectAsState()
    val flashcardList = viewModel.flashcardList.value

    // Dodajmy sprawdzenie, czy currentFlashcard nie jest nullem
    val indexOfFlashcard = if (currentFlashcard != null) {
        flashcardList.indexOf(currentFlashcard)
    } else { -1 }

    if (indexOfFlashcard != -1) {
        val boxUid = flashcardList[indexOfFlashcard].boxUid.toString()
        val flashcardUid = flashcardList[indexOfFlashcard].uid.toString()

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
                {
                    viewModel.updateFlashcardToKnow(
                        boxUid = boxUid,
                        flashcardUid = flashcardUid
                    )
                    viewModel.moveToNextFlashcard(navController)
                },
                {
                    viewModel.updateFlashcardToSomewhatKnow(
                        boxUid = boxUid,
                        flashcardUid = flashcardUid
                    )
                    viewModel.moveToNextFlashcard(navController)
                },
                {
                    viewModel.updateFlashcardToDoNotKnow(
                        boxUid = boxUid,
                        flashcardUid = flashcardUid
                    )
                    viewModel.moveToNextFlashcard(navController)
                }
            )
        }
    }
}
