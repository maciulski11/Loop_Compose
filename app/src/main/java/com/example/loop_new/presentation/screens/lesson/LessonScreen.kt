package com.example.loop_new.presentation.screens.lesson

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Gray
import com.example.loop_new.ui.theme.Gray2
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
//    LessonScreen(boxUid = "")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LessonScreen(navController: NavController, viewModel: LessonViewModel, boxUid: String) {

    val currentFlashcard by viewModel.currentFlashcard.collectAsState()

    var isVisibleLeft by remember { mutableStateOf(false) }
    var isVisibleRight by remember { mutableStateOf(false) }
    var isVisibleUp by remember { mutableStateOf(false) }

    val localDensity = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val swipeableStateX = rememberSwipeableState(initialValue = 0)
    val swipeableStateY = rememberSwipeableState(initialValue = 0)
    val screenHeightPx = with(localDensity) { screenHeight.toPx() }
    val screenWidthPx = with(localDensity) { screenWidth.toPx() }

    val offsetXAnimation = animateFloatAsState(
        targetValue = if (isVisibleLeft) -screenWidthPx else if (isVisibleRight) screenWidthPx else 0f,
        animationSpec = tween(
            durationMillis = 600, easing = LinearOutSlowInEasing
        ),
        label = ""
    ).value

    val offsetYAnimation = animateFloatAsState(
        targetValue = if (isVisibleUp) -screenHeightPx else 0f, animationSpec = tween(
            durationMillis = 600, easing = LinearOutSlowInEasing
        ), label = ""
    ).value

    val offsetXSwipe = swipeableStateX.offset.value
    val offsetYSwipe = swipeableStateY.offset.value

// Użyj offsetYSwipe do obsługi przesuwania i offsetYAnimation dla animacji
    val offsetX = if (isVisibleLeft || isVisibleRight) offsetXAnimation else offsetXSwipe
    val offsetY = if (isVisibleUp) offsetYAnimation else offsetYSwipe

    val anchorsX = mapOf(-screenWidthPx to (-1), 0f to 0, screenWidthPx to 1)
    val anchorsY = mapOf(-screenHeightPx to 2, 0f to 0)

    LaunchedEffect(isVisibleUp || isVisibleLeft || isVisibleRight) {
        if (isVisibleUp || isVisibleLeft || isVisibleRight) {
            delay(500)
            viewModel.moveToNextFlashcard(navController, boxUid)
            isVisibleUp = false
            isVisibleLeft = false
            isVisibleRight = false
        }
    }

    LaunchedEffect(swipeableStateX.currentValue, swipeableStateY.currentValue) {
        if (swipeableStateX.currentValue != 0 || swipeableStateY.currentValue != 0) {
            viewModel.moveToNextFlashcard(navController, boxUid)
            swipeableStateX.snapTo(0)
            swipeableStateY.snapTo(0)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        // Dodaj tekst z numerem bieżącej fiszki i całkowitą liczbą fiszek
        Text(
            text = viewModel.progressText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Gray,
            fontSize = 16.sp
        )

        // Dodaj pasek postępu tutaj
        LinearProgressIndicator(
            progress = viewModel.progress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .padding(start = 12.dp, end = 12.dp) // Marginesy od startu i końca
                .background(Gray2, shape = RoundedCornerShape(6.dp)) // Kolor tła i zaokrąglone rogi
                .clip(RoundedCornerShape(6.dp)), // Zaokrąglone rogi dla samego paska postępu // Kolor tła i zaokrąglone rogi
            color = Blue
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {

            Box(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
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
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 14.dp, end = 14.dp, bottom = 30.dp, top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            Image(painter = painterResource(id = R.drawable.baseline_close_24),
                contentDescription = "Button",
                modifier = Modifier
                    .size(110.dp)
                    .clickable {
                        isVisibleLeft = true
                    })

            Image(painter = painterResource(id = R.drawable.baseline_fitness_center_24),
                contentDescription = "Button",
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        isVisibleUp = true
                    })

            Image(
                painter = painterResource(id = R.drawable.baseline_check_24),
                contentDescription = "Button",
                modifier = Modifier
                    .size(110.dp)
                    .clickable {
                        isVisibleRight = true
                    },
            )
        }
    }
}

@Composable
fun FlashcardItem(flashcardText: Flashcard, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 34.dp, bottom = 44.dp, start = 40.dp, end = 40.dp),
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
            text = flashcardText.pronunciation.toString(),
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
