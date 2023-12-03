package com.example.loop_new.presentation.screens.lesson

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.screens.LessonRepeatScreen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LessonScreen(navController: NavController, viewModel: LessonViewModel, boxUid: String) {

    val currentFlashcard by viewModel.currentFlashcard.collectAsState()
    val flashcardList = viewModel.flashcardList.value
    val indexOfFlashcard = flashcardList.indexOf(currentFlashcard)

    currentFlashcard?.let {
        LessonRepeatScreen(
            flashcardList = flashcardList,
            progressText = viewModel.progressText,
            progress = viewModel.progress,
            currentFlashcard = it,
            { audioUrl ->
                viewModel.playAudioFromUrl(audioUrl)
            },
            {
                viewModel.updateFlashcardToKnow(
                    boxUid,
                    flashcardList[indexOfFlashcard].uid.toString()
                )
                viewModel.moveToNextFlashcard(navController, boxUid)
            },
            {
                viewModel.updateFlashcardToSomewhatKnow(
                    boxUid,
                    flashcardList[indexOfFlashcard].uid.toString()
                )
                viewModel.moveToNextFlashcard(navController, boxUid)
            },
            {
                viewModel.updateFlashcardToDoNotKnow(
                    boxUid,
                    flashcardList[indexOfFlashcard].uid.toString()
                )
                viewModel.moveToNextFlashcard(navController, boxUid)
            },
            {
                navController.navigate("${NavigationSupport.FlashcardScreen}/$boxUid")
            }
        )
    }
}