package com.example.loop_new.presentation.box

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.Navigation
import com.example.loop_new.R
import com.example.loop_new.domain.model.Flashcard
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Orange

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    val navController = rememberNavController()
    val sampleData = createSampleData()

    Screen(navController = navController, boxUid = "", sampleData)
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
    Screen(navController, boxUid, viewModel.flashcardList.value ?: emptyList())
}

@Composable
fun Screen(
    navController: NavController,
    boxUid: String,
    list: List<Flashcard>,
) {
    val context = LocalContext.current // Uzyskaj dostęp do Context za pomocą LocalContext

    val constraints = ConstraintSet {
        val flashcardList = createRefFor("flashcardList")
        val startLesson = createRefFor("startLesson")
        val addFlashcard = createRefFor("addFlashcard")

        constrain(flashcardList) {
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
                .pointerInput(Unit) {
                    detectTapGestures(
                        // Obsługa zdarzenia dotknięcia (LongClick)
                        onLongPress = {
                            //TODO: implement long click
                            Toast.makeText(context, "long clik", Toast.LENGTH_SHORT).show()

                        }
                    )
                }
        ) {
            items(list) { flashcard ->
                FlashcardItem(flashcard)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.start_lesson_circle_78),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("startLesson")
                .clickable {
                }
        )

        Image(
            painter = painterResource(id = R.drawable.baseline_add_circle_box),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("addFlashcard")
                .clickable {
                    navController.navigate("${Navigation.AddFlashcardScreen}/$boxUid")
                }
        )
    }
}

@Composable
fun FlashcardItem(flashcard: Flashcard) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 6.dp,
                start = 2.dp,
                end = 2.dp
            ),
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
                            .padding(top = 12.dp, bottom = 4.dp)
                    )

                    Image(
                        painter = painterResource(
                            id = if (flashcard.audioUrl != null) {
                                R.drawable.baseline_volume
                            } else {
                                R.drawable.baseline_volume_off
                            }
                        ),
                        contentDescription = "Button",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 4.dp, start = 4.dp)
                            .align(Alignment.CenterVertically)
                            .size(32.dp)
                            .clickable {

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
                        .padding(bottom = 12.dp)
                )
            }
        }
    )
}