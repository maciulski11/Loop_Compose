package com.example.loop_new.presentation.screens.lesson

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.example.loop_new.ui.theme.Orange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
//    LessonScreen(boxUid = "")
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LessonScreen(navController: NavController, viewModel: LessonViewModel, boxUid: String) {

    val flashcardList = viewModel.flashcardList.value
    val currentFlashcard by viewModel.currentFlashcard.collectAsState()
    val indexOfFlashcard = flashcardList.indexOf(currentFlashcard)

    var isVisibleLeft by remember { mutableStateOf(false) }
    var isVisibleRight by remember { mutableStateOf(false) }
    var isVisibleUp by remember { mutableStateOf(false) }

    var isFront by remember { mutableStateOf(true) }

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

    LaunchedEffect(
        swipeableStateX.currentValue,
        swipeableStateY.currentValue,
        isVisibleUp,
        isVisibleLeft,
        isVisibleRight
    ) {
        when {

            isVisibleRight || swipeableStateX.currentValue == 1 -> {
                delay(400) // Opóźnienie, aby pozwolić na zakończenie animacji
//                setKnowledgeLevel(KnowledgeLevel.KNOW)
                viewModel.updateFlashcardToKnow(
                    boxUid,
                    flashcardList[indexOfFlashcard].uid.toString()
                )
                viewModel.moveToNextFlashcard(navController, boxUid)
                swipeableStateX.snapTo(0)
                isVisibleRight = false
            }

            isVisibleLeft || swipeableStateX.currentValue == -1 -> {
                delay(400) // Opóźnienie, aby pozwolić na zakończenie animacji
                viewModel.updateFlashcardToSomewhatKnow(
                    boxUid,
                    flashcardList[indexOfFlashcard].uid.toString()
                )
                viewModel.moveToNextFlashcard(navController, boxUid)
                swipeableStateX.snapTo(0)
                isVisibleLeft = false
            }

            isVisibleUp || swipeableStateY.currentValue == 2 -> {
                delay(450) // Opóźnienie, aby pozwolić na zakończenie animacji
                viewModel.updateFlashcardToDoNotKnow(
                    boxUid,
                    flashcardList[indexOfFlashcard].uid.toString()
                )
                viewModel.moveToNextFlashcard(navController, boxUid)
                swipeableStateY.snapTo(0)
                isVisibleUp = false
            }
        }
        isFront = true
    }

    val rotationY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    var isAnimating by remember { mutableStateOf(false) }

    fun triggerRotation() {
        coroutineScope.launch {
            isAnimating = true
            rotationY.animateTo(
                targetValue = if (rotationY.value == 180f) 0f else 180f,
                animationSpec = tween(durationMillis = 550)
            )
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (flashcardList.indexOf(currentFlashcard) < flashcardList.size - 1) {
                    // Renderuj zawartość następnej fiszki, jeśli nie jesteś na ostatniej
                    val flashcard = flashcardList.indexOf(currentFlashcard) + 1

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 40.dp, bottom = 46.dp, start = 46.dp, end = 46.dp)
                            .border(3.dp, Blue, RoundedCornerShape(20.dp))
                            .visible(true) // Create an extension function to handle visibility
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = flashcardList[flashcard].word.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = Color.Black,
                                maxLines = 2,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                            Image(
                                painter = painterResource(
                                    id = if (flashcardList[flashcard].audioUrl!!.isNotEmpty()) {
                                        R.drawable.baseline_volume
                                    } else {
                                        R.drawable.baseline_volume_off
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(42.dp)
                            )
                        }

                        Text(
                            text = flashcardList[flashcard].pronunciation.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp,
                            color = Orange
                        )
                    }
                }
            }

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
                .graphicsLayer {
                    this.rotationY = rotationY.value
                    transformOrigin = TransformOrigin.Center
                    cameraDistance = 12f * density
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch {
                        delay(320)
                        // Resetuj rotację do 0, aby przygotować się na kolejną animację
                        rotationY.snapTo(0f)
                        isFront = !isFront
                        isAnimating = !isAnimating
                    }
                    triggerRotation()
                }
            ) {
                currentFlashcard?.let {
                    FlashcardItem(flashcard = it, isFront, isAnimating) { audioUrl ->
                        viewModel.playAudioFromUrl(audioUrl)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 14.dp, end = 14.dp, bottom = 30.dp, top = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            Image(painter = painterResource(id = R.drawable.baseline_close_24),
                contentDescription = "Button",
                modifier = Modifier
                    .size(110.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isVisibleLeft = true
                    }
            )

            Image(painter = painterResource(id = R.drawable.baseline_fitness_center_24),
                contentDescription = "Button",
                modifier = Modifier
                    .size(100.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isVisibleUp = true
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_check_24),
                contentDescription = "Button",
                modifier = Modifier
                    .size(110.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isVisibleRight = true
                    }
            )
        }
    }
}

@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    front: Boolean,
    isAnimating: Boolean,
    onPlayAudioFromUrl: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 34.dp, bottom = 40.dp, start = 40.dp, end = 40.dp)
            .clip(RoundedCornerShape(20.dp)) // Zaokrąglenie rogu
            .border(3.dp, Blue, RoundedCornerShape(20.dp))
            .background(Color.White), // Białe tło z zaokrąglonymi rogami
        contentAlignment = Alignment.Center
    ) {
        if (!isAnimating) {
            if (front) {
                // The front side of the flashcard
                FrontSide(flashcard) { audioUrl ->
                    onPlayAudioFromUrl(audioUrl)
                }
            } else {
                // The back side of the flashcard
                BackSide(flashcard)
            }
        }
    }
}

@Composable
fun FrontSide(flashcard: Flashcard, onPlayAudioFromUrl: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .visible(true) // Create an extension function to handle visibility
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = flashcard.word.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Black,
                maxLines = 2,
                modifier = Modifier.padding(start = 8.dp)
            )

            Image(
                painter = painterResource(

                    id = if (flashcard.audioUrl!!.isNotEmpty()) {
                        R.drawable.baseline_volume
                    } else {
                        R.drawable.baseline_volume_off
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (flashcard.audioUrl.isNotEmpty()) {
                            onPlayAudioFromUrl(flashcard.audioUrl)
                        }
                    }
            )
        }

        Text(
            text = flashcard.pronunciation.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            color = Orange
        )
    }
}

@Composable
fun BackSide(flashcard: Flashcard) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 22.dp, end = 22.dp)
            .visible(true) // Create an extension function to handle visibility
    ) {
        Text(
            text = flashcard.translate.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            color = Blue,
            maxLines = 2,
        )

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = flashcard.meaning.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Black,
            maxLines = 2,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = flashcard.example.toString(),
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun Modifier.visible(visibility: Boolean): Modifier = if (visibility) {
    this
} else {
    this.then(Modifier.size(0.dp))
}
