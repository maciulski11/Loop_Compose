package com.example.loop_new.presentation.screens.stats_section

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.Black
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Green
import com.example.loop_new.ui.theme.Red
import com.example.loop_new.ui.theme.White

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatsScreenPreview() {
//    StatsScreen()
}

@Composable
fun StatsScreen(navController:NavController, viewModel: StatsViewModel) {

    // Support for custom return behavior
    BackHandler {
        // Where return
        navController.navigate(NavigationSupport.PrivateBoxScreen)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = PaddingValues(0.dp))
            .background(White)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .height(184.dp)
                    .width(124.dp)
                    .padding(top = 42.dp, start = 6.dp)
                    .border(2.dp, Black, RoundedCornerShape(20.dp)),

                ) {

                if (viewModel.statsSummary.value == null) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(38.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = 22.dp)
                    )
                } else {
                    Text(
                        text = viewModel.statsSummary.value?.somewhatKnow.toString(),
                        fontSize = 58.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                Text(
                    text = "Somewhat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp, start = 4.dp, end = 4.dp)
                )

                Text(
                    text = "Know",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp, start = 4.dp, end = 4.dp)
                )
            }


            Box(
                modifier = Modifier
                    .height(184.dp)
                    .width(130.dp)
                    .weight(1f)
                    .padding(top = 18.dp, start = 6.dp, end = 6.dp)
                    .border(2.dp, Black, RoundedCornerShape(20.dp)),

                ) {

                if (viewModel.statsSummary.value == null) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = 22.dp)
                    )
                } else {
                    Text(
                        text = viewModel.statsSummary.value?.knowCount.toString(),
                        fontSize = 70.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                Text(
                    text = "Know",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .height(184.dp)
                    .width(118.dp)
                    .padding(top = 62.dp, end = 6.dp)
                    .border(2.dp, Black, RoundedCornerShape(20.dp)),

                ) {

                if (viewModel.statsSummary.value == null) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = 22.dp)
                    )
                } else {
                    Text(
                        text = viewModel.statsSummary.value?.doNotKnowCount.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Red,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                Text(
                    text = "Do not",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 25.dp, start = 4.dp, end = 4.dp)
                )

                Text(
                    text = "Know",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp, start = 4.dp, end = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(top = 10.dp, start = 6.dp, end = 6.dp)
                .border(2.dp, Black, RoundedCornerShape(14.dp)),
        ) {

            Text(
                text = "All flashcard:",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier
                    .padding(top = 4.dp, start = 12.dp)
            )

            if (viewModel.statsSummary.value == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(22.dp)
                        .padding(top = 14.dp, start = 4.dp)
                )
            } else {
                Text(
                    text = viewModel.statistics.value?.allFlashcards?.size.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 2.dp)
                )
            }
        }
    }
}