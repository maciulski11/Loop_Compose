package com.example.loop_new.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.loop_new.R
import com.example.loop_new.ui.theme.Test
import com.example.loop_new.ui.theme.White


@Preview
@Composable
fun TestScreen() {
   
}


//fun RotatingCheckButton(
//    modifier: Modifier = Modifier,
//    buttonSize: Int = 48,
//    color: Color = Color.Green
//) {
//    var rotationAngle by remember { mutableStateOf(0f) }
//
//    val infiniteTransition = rememberInfiniteTransition()
//    val rotation by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 360f,
//        animationSpec = infiniteRepeatable(
//            animation = keyframes {
//                durationMillis = 2000
//                0f at 0 with LinearEasing // Start rotation
//                360f at 1000 with LinearEasing // End rotation
//            },
//            repeatMode = RepeatMode.Restart
//        ), label = ""
//    )
//
//    LaunchedEffect(key1 = rotation) {
//        rotationAngle = rotation
//    }
//
//    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
//
//
//        Canvas(
//            modifier = modifier
//                .size(buttonSize.dp)
//                .padding(4.dp)
//        ) {
//            drawRect(color = Color.Transparent, size = size)
//            rotate(rotationAngle) {
//                drawLine(
//                    color = color,
//                    start = Offset(0f, size.height / 2),
//                    end = Offset(size.width * 0.3f, size.height * 0.7f),
//                    strokeWidth = 6f
//                )
//                drawLine(
//                    color = color,
//                    start = Offset(size.width * 0.3f, size.height * 0.7f),
//                    end = Offset(size.width.toFloat(), size.height * 0.3f),
//                    strokeWidth = 6f
//                )
//            }
//        }
//    }
//}


//import android.text.Layout
//import android.util.Log
//import androidx.compose.animation.core.MutableTransitionState
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.keyframes
//import androidx.compose.animation.core.snap
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Build
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.loop_new.R
//import com.example.loop_new.ui.theme.Black
//import com.example.loop_new.ui.theme.Blue
//import com.example.loop_new.ui.theme.Green
//import com.example.loop_new.ui.theme.Red
//import com.example.loop_new.ui.theme.White
//import kotlinx.coroutines.launch
//
//data class ListItem(val id: Int, val name: String)
//
//@Preview
//@Composable
//fun LazyListWithLikes() {
//    val items = remember { generateDummyItems() }
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(White)) {
//
//
//        LazyColumn {
//            items(items) { item ->
//
//                Item(name = item.name, id = item.id, item)
//            }
//        }
//    }
//}
//
//@Composable
//fun Item(name:String, id: Int, item: ListItem){
//
//    Row(modifier = Modifier
//        .padding(45.dp)
//        .background(Green)){
//        Text(text = name)
//
//        Text(text = id.toString())
//
//        DoubleTapToLike(item = item) { likedItem ->
//
//            Log.d("LikeClicked", "Item ${likedItem.name} liked")
//        }
//    }
//}
//
//fun generateDummyItems(): List<ListItem> {
//    return listOf(
//        ListItem(1, "Item 1"),
//        ListItem(2, "Item 2"),
//        ListItem(3, "Item 3"),
//    )
//}
//
//@Composable
//private fun DoubleTapToLike(
//    item: ListItem,
//    onLikeClicked: (ListItem) -> Unit
//) {
//    var transitionState by remember(item.id) {
//        mutableStateOf(MutableTransitionState(LikedStates.Disappeared))
//    }
//
//    Box(
//        Modifier
//            .width(50.dp)
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onDoubleTap = {
//                        transitionState = MutableTransitionState(LikedStates.Initial)
//                        onLikeClicked(item)
//                    }
//                )
//            }
//    ) {
//        if (transitionState.currentState == LikedStates.Initial) {
//            transitionState.targetState = LikedStates.Liked
//        } else if (transitionState.currentState == LikedStates.Liked) {
//            transitionState.targetState = LikedStates.Disappeared
//        }
//
//        val transition = updateTransition(transitionState = transitionState, label = null)
//        val alpha by transition.animateFloat(
//            transitionSpec = {
//                when {
//                    LikedStates.Initial isTransitioningTo LikedStates.Liked ->
//                        keyframes {
//                            durationMillis = 500
//                            0f at 0
//                            0.5f at 100
//                            1f at 225
//                        }
//                    LikedStates.Liked isTransitioningTo LikedStates.Disappeared ->
//                        tween(durationMillis = 200)
//                    else -> snap()
//                }
//            },
//            label = "DoubleTapToLikeAlpha",
//        ) {
//            if (it == LikedStates.Liked) 1f else 0f
//        }
//
//        val scale by transition.animateFloat(
//            transitionSpec = {
//                when {
//                    LikedStates.Initial isTransitioningTo LikedStates.Liked ->
//                        spring(dampingRatio = Spring.DampingRatioHighBouncy)
//                    LikedStates.Liked isTransitioningTo LikedStates.Disappeared ->
//                        tween(200)
//                    else -> snap()
//                }
//            },
//            label = "DoubleTapToLikeScale",
//        ) {
//            when (it) {
//                LikedStates.Initial -> 0f
//                LikedStates.Liked -> 4f
//                LikedStates.Disappeared -> 2f
//            }
//        }
//
//        Icon(
//            Icons.Filled.Favorite,
//            stringResource(id = R.string.app_name),
//            Modifier
//                .align(Alignment.Center)
//                .graphicsLayer(
//                    alpha = alpha,
//                    scaleX = scale,
//                    scaleY = scale
//                ),
//            tint = Color.Red
//        )
//    }
//}
//
//enum class LikedStates {
//    Initial,
//    Liked,
//    Disappeared
//}















