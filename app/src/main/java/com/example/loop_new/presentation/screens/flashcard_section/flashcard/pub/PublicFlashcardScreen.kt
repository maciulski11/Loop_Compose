package com.example.loop_new.presentation.screens.flashcard_section.flashcard.pub

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.presentation.screens.flashcard_section.flashcard.FlashcardItem
import com.example.loop_new.ui.theme.Green
import kotlin.math.roundToInt

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PublicScreenPreview() {
//    val sampleData = publicCreateSampleData()

//    PublicScreen(sampleData, { }, { })
}

//@Composable
//fun publicCreateSampleData(): List<Flashcard> {
//    val sampleData = mutableListOf<Flashcard>()
//
//    for (i in 1..12) {
//        sampleData.add(Flashcard(word = "Flashcard", pronunciation = "(h)wer"))
//    }
//    return sampleData
//}

// UI
@Composable
fun PublicFlashcardScreen(
    navController: NavController,
    box: Box,
    viewModel: PublicFlashcardViewModel,
) {

    // Support for custom return behavior
    BackHandler {
        navController.navigate(NavigationSupport.BoxScreen)
    }


    val flashcards = viewModel.flashcardList.value

    val boxAddedToDatabase = remember { mutableStateOf(false) }

    PublicScreen(
        list = flashcards,
        onPlayAudioFromUrl = { audioUrl ->
            viewModel.playAudioFromUrl(audioUrl)
        },
        addPublicBoxToPrivateSection = {
            if (!boxAddedToDatabase.value) {
                val dataOfBox = box.copy(
                    permissionToEdit = false,
                    addFlashcardFromStory = false
                )

                val uniqueFlashcards = mutableListOf<Flashcard>()

                for (flashcard in viewModel.flashcardList.value) {
                    val isDuplicate = uniqueFlashcards.any { it.uid == flashcard.uid }
                    if (!isDuplicate) {
                        uniqueFlashcards.add(flashcard)
                    }
                }

                Log.d("PublicFlashcardScreen", "Unique flashcards to add: ${uniqueFlashcards.size}")
                uniqueFlashcards.forEach { Log.d("PublicFlashcardScreen", "Flashcard: $it") }

                viewModel.addPublicBoxToPrivateSection(dataOfBox, uniqueFlashcards)
                boxAddedToDatabase.value = true
            }
            navController.navigate("${NavigationSupport.LessonScreen}/${box.uid}")
        }
    )
}

@Composable
fun PublicScreen(
    list: List<Flashcard>,
    onPlayAudioFromUrl: (String) -> Unit,
    addPublicBoxToPrivateSection: () -> Unit,
) {
    val showDialogDeleteFlashcard = remember { mutableStateOf(false) }

    val constraints = ConstraintSet {
        val flashcardsList = createRefFor("flashcardList")
        val slideButton = createRefFor("slideButton")

        constrain(flashcardsList) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(slideButton) {
            bottom.linkTo(parent.bottom, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    ConstraintLayout(
        constraints,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("flashcardList")
        ) {
            items(list) { flashcard ->
                FlashcardItem(
                    flashcard = flashcard,
                    onPlayAudioFromUrl = { audioUrl ->
                        onPlayAudioFromUrl(audioUrl)
                    },
                    onLongPress = { longClick ->
                        showDialogDeleteFlashcard.value = longClick
                    },
                    onClick = {
                        // without clik in public section
                    }
                )
            }
        }

        SlideToUnlockButton(
            onSlideComplete = {
                addPublicBoxToPrivateSection()
            }
        )
    }
}

@Composable
fun SlideToUnlockButton(onSlideComplete: () -> Unit) {
    val dragDistance = remember { mutableStateOf(0f) }
    val maxDragDistance = remember { mutableStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .height(44.dp)
            .width(268.dp)
            .layoutId("slideButton")
    ) {
        val sliderWidthPx = with(LocalDensity.current) { 92.dp.toPx() }
        maxDragDistance.value = constraints.maxWidth - sliderWidthPx

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(4.dp) // Wysokość paska
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Black.copy(alpha = 0.5f)) // Półprzezroczysty czarny kolor
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(dragDistance.value.roundToInt(), 0) }
                .fillMaxHeight()
                .width(92.dp)
                .background(Green, shape = RoundedCornerShape(50))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { dragDistance.value = 0f },
                        onDragEnd = { dragDistance.value = 0f }
                    ) { change, dragAmount ->
                        val newDragDistance = dragDistance.value + dragAmount.x
                        dragDistance.value = newDragDistance.coerceIn(0f, maxDragDistance.value)
                        change.consumeAllChanges()

                        if (dragDistance.value >= maxDragDistance.value) {
                            onSlideComplete()
                        }
                    }
                }
        ) {
            Text(
                text = "Let's start!",
                modifier = Modifier
                    .align(Alignment.Center),
                fontSize = 17.sp,
                color = White
            )
        }
    }
}