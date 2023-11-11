package com.example.loop_new.presentation.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.ui.theme.Blue
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    FlashcardItem("dsfdsfd")
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LessonScreen(viewModel: LessonViewModel) {
    val currentFlashcard by viewModel.currentFlashcard.collectAsState()
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()

    val localDensity = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val swipeableStateX = rememberSwipeableState(initialValue = 0)
    val swipeableStateY = rememberSwipeableState(initialValue = 0)
    val screenHeightPx = with(localDensity) { screenHeight.toPx() }
    val screenWidthPx = with(localDensity) { screenWidth.toPx() }

    val anchorsX = mapOf(-screenWidthPx to (-1), 0f to 0, screenWidthPx to 1)
    val anchorsY = mapOf(-screenHeightPx to 2, 0f to 0)

    LaunchedEffect(swipeableStateX.currentValue, swipeableStateY.currentValue) {
        if (swipeableStateX.currentValue != 0 || swipeableStateY.currentValue != 0) {
            viewModel.moveToNextFlashcard()
            swipeableStateX.snapTo(0)
            swipeableStateY.snapTo(0)
        }
    }

    if (isDataLoaded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            val offsetX = swipeableStateX.offset.value.roundToInt()
            val offsetY = swipeableStateY.offset.value.roundToInt()

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = screenWidth, height = screenHeight)
                    .offset { IntOffset(offsetX, offsetY) }
                    .swipeable(
                        state = swipeableStateX,
                        anchors = anchorsX,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
                    .swipeable(
                        state = swipeableStateY,
                        anchors = anchorsY,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Vertical
                    )
            ) {
                currentFlashcard?.let {
                    FlashcardItem(flashcardText = it)
                } ?: Text("Brak fiszek do wyświetlenia")
            }
        }
    } else {
        // Możesz tutaj dodać ekran ładowania
        Text("Ładowanie danych...", modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun FlashcardItem(flashcardText: Flashcard, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 74.dp, bottom = 78.dp, start = 40.dp, end = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 14.dp, end = 14.dp),
                fontSize = 16.sp
            )

            // The front side of the flashcard
            FrontSide(flashcardText)

            // The back side of the flashcard
            BackSide()
        }
    }
}

@Composable
fun FrontSide(flashcardText: Flashcard) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .visible(true) // Create an extension function to handle visibility
            .border(3.dp, Blue, RoundedCornerShape(20.dp))
        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = flashcardText.word.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black,
                maxLines = 2,
                modifier = Modifier.padding(start = 8.dp)
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Text(
            text = "dsvdv",
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            color = Color(0xFFFFA500) // This is the color orange in ARGB
        )
    }
}

@Composable
fun BackSide() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.visible(false) // Create an extension function to handle visibility
    ) {
        Text(
            text = "",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.Black // Replace with the correct color from resources
        )
        Text(
            text = "a thing done successfully, typically by effort, courage, or skill.",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = "to reach this stage is a great achievement",
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        )
    }
}

@Composable
fun Modifier.visible(visibility: Boolean): Modifier = if (visibility) {
    this
} else {
    this.then(Modifier.size(0.dp))
}
