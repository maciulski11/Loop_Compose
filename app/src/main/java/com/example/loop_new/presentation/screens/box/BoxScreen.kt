package com.example.loop_new.presentation.screens.box

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Orange

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
fun BoxScreen(navController: NavController, boxUid: String, viewModel: BoxViewModel) {

    Screen(
        navController,
        boxUid,
        viewModel.flashcardList.value ?: emptyList(),
        { audioUrl ->
            viewModel.playAudioFromUrl(audioUrl)
        },
        { flashcardUid ->
            viewModel.deleteFlashcard(boxUid, flashcardUid)
        }
    )
}

@Composable
fun Screen(
    navController: NavController,
    boxUid: String,
    list: List<Flashcard>,
    onPlayAudioFromUrl: (String) -> Unit,
    onDeleteFlashcard: (String) -> Unit
) {
    val constraints = ConstraintSet {
        val flashcardsList = createRefFor("flashcardList")
        val startLesson = createRefFor("startLesson")
        val addFlashcard = createRefFor("addFlashcard")

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
                    flashcard,
                    { audioUrl ->
                        onPlayAudioFromUrl(audioUrl)

                    },
                    { flashcardUid ->
                        onDeleteFlashcard(flashcardUid)
                    }
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.start_lesson_circle_78),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("startLesson")
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    navController.navigate("${NavigationSupport.LessonScreen}/$boxUid")
                }
        )

        Image(
            painter = painterResource(id = R.drawable.baseline_add_circle_box),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("addFlashcard")
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    navController.navigate("${NavigationSupport.AddFlashcardScreen}/$boxUid")
                }
        )
    }
}

@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    onPlayAudioFromUrl: (String) -> Unit,
    onDeleteFlashcard: (String) -> Unit
) {
    val showDialogState = remember { mutableStateOf(false) }

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
                    // Obsługa zdarzenia dotknięcia (LongClick)
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
                    .border(3.dp, Blue, RoundedCornerShape(20.dp)),
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

    // Delete flashcard alert dialog
    if (showDialogState.value) {
        ShowCustomAlertDialog(
            flashcard.word.toString(),
            {
                onDeleteFlashcard(flashcard.uid.toString())
            }
        ) {
            showDialogState.value = false
        }
    }
}

@Composable
fun ShowCustomAlertDialog(
    word: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val deleteFlashcard = buildAnnotatedString {
        append("Do you want to delete flashcard: ")
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        )
        append(word)
        pop() // Kończy zastosowanie pogrubienia
        append("?")
    }

    AlertDialog(
        modifier = Modifier
            .height(150.dp)
            .width(300.dp)
            .clip(RoundedCornerShape(20.dp)) // Zaokrąglenie rogu
            .border(3.dp, Black, RoundedCornerShape(20.dp))
            .background(White), // Białe tło z zaokrąglonymi rogami
        onDismissRequest = { /* Touching the screen turns off it */ },
        title = {
            Text(
                text = deleteFlashcard,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 20.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally) // Wyśrodkowanie w poziomie
            )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 22.dp),
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Button(
                    // Button background
                    colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                    onClick = {
                        onDelete()
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = Black
                    )
                }
            }
        }
    )
}