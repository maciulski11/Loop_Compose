package com.example.loop_new.presentation.screens.flashcard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Green
import com.example.loop_new.ui.theme.Orange
import com.example.loop_new.ui.theme.Red
import kotlin.math.roundToInt

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    val navController = rememberNavController()
    val sampleData = createSampleData()

    Screen(navController = navController, boxUid = "", sampleData, { }, { })
}

@Composable
fun createSampleData(): List<Flashcard> {
    val sampleData = mutableListOf<Flashcard>()

    for (i in 1..12) {
        sampleData.add(Flashcard(word = "Flashcard", pronunciation = "(h)wer"))
    }
    return sampleData
}

// UI
@Composable
fun FlashcardScreen(navController: NavController, boxUid: String, viewModel: FlashcardViewModel) {

    // Support for custom return behavior
    BackHandler {
        // Where return
        navController.navigate(NavigationSupport.BoxScreen)
    }

    Screen(
        navController,
        boxUid,
        viewModel.flashcardList.value ?: emptyList(),
        { audioUrl ->
            viewModel.playAudioFromUrl(audioUrl)
        },
        {
            viewModel.addPublicBoxToPrivateBox(boxUid)
        }
    )
}

@Composable
fun Screen(
    navController: NavController,
    boxUid: String,
    list: List<Flashcard>,
    onPlayAudioFromUrl: (String) -> Unit,
    addPublicBoxToPrivateSection: () -> Unit,
) {
    val constraints = ConstraintSet {
        val flashcardsList = createRefFor("flashcardList")
        val startLesson = createRefFor("startLesson")
        val addFlashcard = createRefFor("addFlashcard")
        val slideButton = createRefFor("slideButton")

        constrain(flashcardsList) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(startLesson) {
            bottom.linkTo(parent.bottom, margin = 92.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }

        constrain(addFlashcard) {
            bottom.linkTo(parent.bottom, margin = 14.dp)
            end.linkTo(parent.end, margin = 16.dp)
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
                    flashcard
                ) { audioUrl ->
                    onPlayAudioFromUrl(audioUrl)
                }
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
fun FlashcardItem(
    flashcard: Flashcard,
    onPlayAudioFromUrl: (String) -> Unit,
) {
    val showDialogState = remember { mutableStateOf(false) }
    val color = remember { mutableStateOf(Black) } // Domyślny kolor

    when (flashcard.knowledgeLevel) {
        KnowledgeLevel.KNOW.value -> color.value = Green
        KnowledgeLevel.DO_NOT_KNOW.value -> color.value = Red
        KnowledgeLevel.SOMEWHAT_KNOW.value -> color.value = Blue
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 6.dp,
                start = 2.dp,
                end = 2.dp
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    // LongClick
                    onLongPress = {
                        showDialogState.value = true
                    }
                )
            },
        contentAlignment = Alignment.Center,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, color.value, RoundedCornerShape(20.dp)),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = flashcard.word.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 2.dp)
                    )

                    Image(
                        painter = painterResource(

                            id = if (flashcard.audioUrl!!.isNotEmpty()) {
                                R.drawable.baseline_volume
                            } else {
                                R.drawable.baseline_volume_off
                            }
                        ),
                        contentDescription = "Button",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 2.dp, start = 4.dp)
                            .align(Alignment.CenterVertically)
                            .size(32.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (flashcard.audioUrl.isNotEmpty()) {
                                    onPlayAudioFromUrl(flashcard.audioUrl)
                                }
                            }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                }

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = flashcard.pronunciation.toString(),
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center,
                    color = Orange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 14.dp)
                )
            }
        }
    )
}

@Composable
fun SlideToUnlockButton(onSlideComplete: () -> Unit) {
    val dragDistance = remember { mutableStateOf(0f) }
    val maxDragDistance = remember { mutableStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .height(44.dp) // Wysokość całego komponentu
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