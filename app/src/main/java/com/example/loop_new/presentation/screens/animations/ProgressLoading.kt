package com.example.loop_new.presentation.screens.animations

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
fun ProgressLoading() {
    val compositionInfinity by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_infinity))
    val compositionDots by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_dots))

    var isPlaying by remember { mutableStateOf(true) }

    val progress by animateLottieCompositionAsState(
        composition = compositionInfinity,
        isPlaying = isPlaying
    )

    LaunchedEffect(key1 = progress) {
        if (progress == 1f) {
            isPlaying = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ) {

        LottieAnimation(
            modifier = Modifier
                .size(368.dp)
                .align(Alignment.Center)
                .padding(bottom = 58.dp),
            composition = compositionInfinity,
            progress = { progress }
        )

        Text(
            text = "Loading",
            color = Test,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 94.dp, end = 20.dp),
        )

        LottieAnimation(
            modifier = Modifier
                .height(160.dp)
                .width(110.dp)
                .align(Alignment.Center)
                .padding(top = 104.dp, start = 60.dp),
            composition = compositionDots,
            progress = { progress }
        )

        isPlaying = true
    }
}