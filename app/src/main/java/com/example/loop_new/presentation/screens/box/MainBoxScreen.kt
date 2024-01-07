package com.example.loop_new.presentation.screens.box

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.ui.theme.Black

@Composable
fun AnimatedLearningButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val size by infiniteTransition.animateValue(
        initialValue = 66.dp,
        targetValue = 98.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Column(
        modifier = Modifier
            .size(size)
            .layoutId("repeatButton")
            .background(Color.Transparent)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
    ) {

        Image(
            painter = painterResource(id = R.drawable.flashcard4),
            contentDescription = "Learning Image",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .weight(1f)
        )
    }
}

@Composable
fun BoxItem(box: Box, onClick: () -> Unit, onLongClick: () -> Unit) {

    // Convert HEX to Color
    val color1 = hexToColor(box.color1 ?: "F0EFEF")
    val color2 = hexToColor(box.color2 ?: "C9C9CC")
    val color3 = hexToColor(box.color3 ?: "B3B5B8")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                    },
                    onTap = {
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center,
        content = {
            Row {
                Box(
                    modifier = Modifier
                        .size(190.dp, 160.dp)
                        .weight(1f)
                        .padding(top = 4.dp, bottom = 4.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .size(172.dp, 155.dp)
                            .background(color3, shape = RoundedCornerShape(10.dp))
                            .border(2.dp, Black, shape = RoundedCornerShape(10.dp))
                    )

                    Box(
                        modifier = Modifier
                            .size(180.dp, 144.dp)
                            .align(Alignment.BottomCenter)
                            .background(color2, shape = RoundedCornerShape(10.dp))
                            .border(2.dp, Black, shape = RoundedCornerShape(10.dp))
                    )

                    Box(
                        modifier = Modifier
                            .size(190.dp, 130.dp)
                            .align(Alignment.BottomCenter)
                            .background(color1, shape = RoundedCornerShape(10.dp))
                            .border(2.dp, Black, shape = RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = box.name.toString(),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            minLines = 1,
                            maxLines = 2,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(
                                    top = 14.dp,
                                    start = 10.dp,
                                    end = 10.dp
                                )
                        )

                        Text(
                            text = box.describe.toString(),
                            style = TextStyle(color = Color.Black, fontSize = 14.5.sp),
                            maxLines = 3,
                            modifier = Modifier
                                .padding(
                                    top = 70.dp,
                                    start = 12.dp,
                                    end = 10.dp
                                )
                        )
                    }
                }
            }
        }
    )
}

// HEX to Color conversion
private fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}