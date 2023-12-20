package com.example.loop_new.presentation.screens.boxUser

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.White
import kotlin.math.roundToInt

@Composable
fun BoxUSerScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Bottom,
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

    // Zmniejsz wysokość tła przesuwania
    BoxWithConstraints(
        modifier = Modifier
            .height(50.dp) // Zmniejszona wysokość tła
            .fillMaxWidth()
            .padding(horizontal = 72.dp)
            .background(Color.Transparent)
    ) {
        val sliderWidthPx = with(LocalDensity.current) { 84.dp.toPx() }
        maxDragDistance.value = constraints.maxWidth - sliderWidthPx

        // Wąski czarny pasek za przyciskiem
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(4.dp) // Wysokość paska
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color.Black.copy(alpha = 0.5f)) // Półprzezroczysty czarny kolor
        )

        // Zwiększ wysokość przycisku i zastosuj zaokrąglone rogi
        Box(
            modifier = Modifier
                .offset { IntOffset(dragDistance.value.roundToInt(), 0) }
                .fillMaxHeight()
                .width(84.dp)
                .background(Blue, shape = RoundedCornerShape(50)) // Bardziej owalny kształt
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
                fontSize = 15.sp,
                color = White
            )
        }
    }
}






