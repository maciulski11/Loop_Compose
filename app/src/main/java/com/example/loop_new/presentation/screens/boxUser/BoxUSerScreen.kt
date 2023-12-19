package com.example.loop_new.presentation.screens.boxUser

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Gray
import kotlin.math.roundToInt

@Composable
fun BoxUSerScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SlideToUnlockButton(onSlideComplete = {
            // Coś się dzieje, gdy przycisk jest przesunięty do końca
            Log.d("SlideButton", "Przycisk przesunięty!")
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()

        })
    }
}

@Composable
fun SlideToUnlockButton(onSlideComplete: () -> Unit) {
    val dragDistance = remember { mutableStateOf(0f) }
    val maxDragDistance = remember { mutableStateOf(0f) }
    val isCompleted = remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .background(Gray)
            .padding(8.dp)
            .height(50.dp)
            .width(220.dp)
    ) {
        val sliderWidthPx = with(LocalDensity.current) { 50.dp.toPx() }
        maxDragDistance.value = constraints.maxWidth - sliderWidthPx

        Box(
            modifier = Modifier
                .offset { IntOffset(dragDistance.value.roundToInt(), 0) }
                .background(Blue)
                .fillMaxHeight()
                .width(50.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isCompleted.value = false
                        },
                        onDragEnd = {
                            dragDistance.value = 0f
                            isCompleted.value = false
                        }
                    ) { change, dragAmount ->
                        val newDragDistance = dragDistance.value + dragAmount.x
                        dragDistance.value = newDragDistance.coerceIn(0f, maxDragDistance.value)
                        change.consumeAllChanges()

                        if (dragDistance.value >= maxDragDistance.value && !isCompleted.value) {
                            onSlideComplete()
                            isCompleted.value = true
                        }
                    }
                }
        )
    }
}





