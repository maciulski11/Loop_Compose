package com.example.loop_new.presentation.screens.box

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.example.loop_new.ui.theme.*
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.loop_new.presentation.screens.box.ShowCustomAlertDialog as ShowCustomAlertDialog

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
//        sampleData.add(Box("Box $i", "Description $i"))
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
    ) { nameInput, describeInput, groupColor ->
        viewModel.addBox(nameInput, describeInput, groupColor)
    }
}

@Composable
fun Screen(
    navController: NavController,
    list: List<Box>,
    viewModel: BoxViewModel,
    onAddBox: (nameInput: String, describeInput: String, colors: List<Color>) -> Unit,
) {

    BackHandler { /* gesture return is off */ }

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
            bottom.linkTo(parent.bottom, margin = 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    val itemsInRow = 2 // Ilość elementów w jednym wierszu

    ConstraintLayout(
        constraints,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
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
                { nameInput, describeInput, groupColor ->
                    onAddBox(nameInput, describeInput, groupColor)
                },
                {
                    showDialogState.value = false
                },
                showDialogState.value
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

enum class SnackbarMessage {
    None,
    MaxCharactersExceededName,
    MaxCharactersExceededDescription
}

@Composable
fun ShowCustomAlertDialog(
    onAddBox: (nameInput: String, describeInput: String, colors: List<Color>) -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean
) {
    var nameInput by remember { mutableStateOf("") }
    var describeInput by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(PastelWhite) }
    // // Initialize the selected Color Group with the default color group
    var selectedColorGroup by remember {
        mutableStateOf(listOf(PastelWhite, PastelWhite1, PastelWhite2))
    }
    val colorGroups = listOf(
        listOf(PastelMint, PastelMint1, PastelMint2),
        listOf(PastelYellow, PastelYellow1, PastelYellow2),
        listOf(PastelRose, PastelRose1, PastelRose2),
        listOf(PastelWhite, PastelWhite1, PastelWhite2),
        listOf(PastelBlue, PastelBlue1, PastelBlue2),
        listOf(PastelPurple, PastelPurple1, PastelPurple2)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf(SnackbarMessage.None) }

    // Display Snackbar
    LaunchedEffect(snackbarMessage) {
        when (snackbarMessage) {
            SnackbarMessage.MaxCharactersExceededName -> {
                snackbarHostState.showSnackbar(
                    message = "The maximum number of characters for box name is 35!",
                    duration = SnackbarDuration.Short
                )
            }

            SnackbarMessage.MaxCharactersExceededDescription -> {
                snackbarHostState.showSnackbar(
                    message = "The maximum number of characters for description is 60!",
                    duration = SnackbarDuration.Short
                )
            }

            SnackbarMessage.None -> { /* Don't do anything */
            }
        }
        snackbarMessage = SnackbarMessage.None // Status reset
    }

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .width(350.dp)
                    .heightIn(min = 300.dp, max = 450.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Create Box:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(colorGroups) { group ->
                            val representativeColor = group.first()
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(representativeColor, shape = CircleShape)
                                    .border(1.dp, Black, shape = CircleShape)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        backgroundColor = representativeColor
                                        selectedColorGroup = group
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = nameInput,
                        maxLines = 2,
                        onValueChange = {
                            if (it.length <= 35) {
                                nameInput = it
                            } else {
                                snackbarMessage = SnackbarMessage.MaxCharactersExceededName
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Go,
                        ),
                        placeholder = { Text("Box Name - 35 characters") }
                    )

                    // Licznik znaków jako adnotacja
                    Text(
                        text = "${nameInput.length}/${35}",
                        modifier = Modifier
                            .padding(top = 4.dp, end = 12.dp)
                            .align(Alignment.End),
                        style = TextStyle(color = Gray),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = describeInput,
                        maxLines = 3,
                        onValueChange = {
                            if (it.length <= 60) {
                                describeInput = it
                            } else {
                                snackbarMessage = SnackbarMessage.MaxCharactersExceededDescription
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Go,
                        ),
                        placeholder = { Text("Description - 60 characters") }
                    )

                    Text(
                        text = "${describeInput.length}/${60}",
                        modifier = Modifier
                            .padding(top = 4.dp, end = 12.dp)
                            .align(Alignment.End),
                        style = TextStyle(color = Gray),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 22.dp),
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {

                        Button(
                            modifier = Modifier
                                .height(40.dp)
                                .width(84.dp),
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
                            modifier = Modifier
                                .height(40.dp)
                                .width(84.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                            onClick = {
                                onAddBox(nameInput, describeInput, selectedColorGroup)
                                onDismiss()
                            }
                        ) {
                            Text(
                                text = "OK",
                                color = Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Kontener na Snackbar
                    SnackbarHost(hostState = snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun BoxItem(box: Box, navController: NavController) {

    // Convert HEX to Color
    val color1 = hexToColor(box.color1 ?: "F0EFEF")
    val color2 = hexToColor(box.color2 ?: "C9C9CC")
    val color3 = hexToColor(box.color3 ?: "B3B5B8")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                val boxUid = box.uid
                navController.navigate("${NavigationSupport.FlashcardScreen}/$boxUid")
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

                        Text(
                            text = "1/50",
                            style = TextStyle(color = Black, fontSize = 12.sp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 8.dp, end = 12.dp)
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



