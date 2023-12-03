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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    val navController = rememberNavController()
    val sampleData = createSampleData()

//    Screen(navController, sampleData) { _, _ -> }
}

@Composable
fun createSampleData(): List<Box> {
    val sampleData = mutableListOf<Box>()

    for (i in 1..22) {
        sampleData.add(Box("Box $i", "Description $i"))
    }
    return sampleData
}

// UI
@Composable
fun BoxScreen(navController: NavController, viewModel: BoxViewModel) {

    Screen(
        navController = navController,
        list = viewModel.boxList.value ?: emptyList(),
        viewModel,
    ) { nameInput, describeInput ->
        viewModel.addBox(nameInput, describeInput)
    }
}

@Composable
fun Screen(
    navController: NavController,
    list: List<Box>,
    viewModel: BoxViewModel,
    onAddBox: (nameInput: String, describeInput: String) -> Unit,
) {
    val showDialogState = remember { mutableStateOf(false) }

    val constraints = ConstraintSet {
        val boxList = createRefFor("boxList")
        val addBoxButton = createRefFor("addBoxButton")
        val repeatButton = createRefFor("repeatButton")

        constrain(boxList) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(addBoxButton) {
            bottom.linkTo(parent.bottom, margin = 14.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }

        constrain(repeatButton) {
            bottom.linkTo(parent.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    val itemsInRow = 2 // Ilość elementów w jednym wierszu

    ConstraintLayout(
        constraints,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("boxList"),
            columns = GridCells.Fixed(itemsInRow)
        ) {
            items(list) { box ->
                BoxItem(box, navController)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.baseline_add_circle_box),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("addBoxButton")
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showDialogState.value = true
                }
        )

        if (showDialogState.value) {
            ShowCustomAlertDialog(
                { nameInput, describeInput ->
                    onAddBox(nameInput, describeInput)
                },
                {
                    showDialogState.value = false
                }
            )
        }

        if (!viewModel.isListEmpty) {
            AnimatedLearningButton(
                onClick = {
                    navController.navigate(NavigationSupport.RepeatScreen)
                }
            )
        }
    }
}

@Composable
fun ShowCustomAlertDialog(
    onAddBox: (nameInput: String, describeInput: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var nameInput by remember { mutableStateOf("") }
    var describeInput by remember { mutableStateOf("") }

    AlertDialog(
        modifier = Modifier
            .height(270.dp)
            .width(340.dp)
            .clip(RoundedCornerShape(20.dp)) // Zaokrąglenie rogu
            .border(3.dp, Black, RoundedCornerShape(20.dp))
            .background(White), // Białe tło z zaokrąglonymi rogami
        onDismissRequest = { /* Touching the screen turns off it */ },
        title = {
            Text(
                text = "Create Box:",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        },
        text = {
            Column {
                TextField(
                    value = nameInput,
                    placeholder = { Text("name") },
                    onValueChange = { nameInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                TextField(
                    value = describeInput,
                    placeholder = { Text("description") },
                    onValueChange = { describeInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 22.dp),
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    onClick = {
                        onAddBox(nameInput, describeInput)
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "OK",
                        color = Black
                    )
                }
            }
        }
    )
}

@Composable
fun BoxItem(box: Box, navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 6.dp,
                start = 2.dp,
                end = 2.dp
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                val boxUid = box.uid
                navController.navigate("${NavigationSupport.FlashcardScreen}/$boxUid")
            },
        contentAlignment = Alignment.Center,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, Black, RoundedCornerShape(20.dp)),
            ) {
                Text(
                    text = box.name.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp, bottom = 6.dp)
                )

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = box.describe.toString(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp)
                )
            }
        }
    )
}

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
            .background(Transparent)
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



