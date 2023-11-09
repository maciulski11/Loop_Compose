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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.loop_new.ui.theme.Blue
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlashcardItem("dsfdsfd")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LessonScreen() {
    val localDensity = LocalDensity.current

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val items = listOf("1", "2", "3", "4", "5", "6", "7")
    val currentItem = remember { mutableStateOf(0) }

    val swipeableState = rememberSwipeableState(initialValue = 0)
    val screenWidthPx = with(localDensity) { screenWidth.toPx() }
    val anchors = mapOf(
        -screenWidthPx to (-1), // Full swipe to the left
        0f to 0, // Center position
        screenWidthPx to 1 // Full swipe to the right
    )

    LaunchedEffect(swipeableState.currentValue) {
        when (swipeableState.currentValue) {
            -1, 1 -> {
                // Inkrementujemy currentItem, ale nie pozwalamy mu wyjść poza zakres listy
                currentItem.value = (currentItem.value + 1) % items.size
                swipeableState.snapTo(0) // Snap back to center after changing the item
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 16.dp,
                    bottom = 26.dp
                )
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            FlashcardItem(flashcardText = items[currentItem.value])
        }
    }
}

@Composable
fun FlashcardItem(flashcardText: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 74.dp, bottom = 78.dp, start = 40.dp, end = 40.dp)
//            .background(painterResource(id = R.drawable.around_background))
            ,
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
fun FrontSide(flashcardText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .visible(true) // Create an extension function to handle visibility
            .border(3.dp, Blue, RoundedCornerShape(20.dp))
        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = flashcardText,
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
