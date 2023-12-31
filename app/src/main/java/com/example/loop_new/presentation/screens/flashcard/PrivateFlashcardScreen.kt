package com.example.loop_new.presentation.screens.flashcard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.Black
import com.example.loop_new.ui.theme.Red
import com.example.loop_new.ui.theme.White

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrivateScreenPreview() {
    val navController = rememberNavController()
    val sampleData = privateCreateSampleData()

//    PrivateScreen(navController = navController, boxUid = "", sampleData, { }, { })
}

@Composable
fun privateCreateSampleData(): List<Flashcard> {
    val sampleData = mutableListOf<Flashcard>()

    for (i in 1..12) {
        sampleData.add(Flashcard(word = "Flashcard", pronunciation = "(h)wer"))
    }
    return sampleData
}

// UI
@Composable
fun PrivateFlashcardScreen(
    navController: NavController,
    boxUid: String,
    viewModel: PrivateFlashcardViewModel,
) {
    // Support for custom return behavior
    BackHandler {
        // Where return
        navController.navigate(NavigationSupport.PrivateBoxScreen)
    }

    PrivateScreen(
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
fun PrivateScreen(
    navController: NavController,
    boxUid: String,
    list: List<Flashcard>,
    onPlayAudioFromUrl: (String) -> Unit,
    onDeleteFlashcard: (String) -> Unit,
) {
    val showDialogState = remember { mutableStateOf(false) }

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
                    flashcard,
                    { audioUrl ->
                        onPlayAudioFromUrl(audioUrl)
                    },
                    { longClick ->
                        showDialogState.value = longClick
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
        pop() // Ends the application of bold
        append("?")
    }

    AlertDialog(
        modifier = Modifier
            .height(150.dp)
            .width(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(3.dp, Black, RoundedCornerShape(20.dp))
            .background(White),
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