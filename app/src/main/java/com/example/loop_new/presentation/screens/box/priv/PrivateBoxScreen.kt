package com.example.loop_new.presentation.screens.box.priv

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.screens.box.AnimatedLearningButton
import com.example.loop_new.presentation.screens.box.BoxItem
import com.example.loop_new.ui.theme.Black
import com.example.loop_new.ui.theme.Gray
import com.example.loop_new.ui.theme.Green
import com.example.loop_new.ui.theme.PastelBlue
import com.example.loop_new.ui.theme.PastelBlue1
import com.example.loop_new.ui.theme.PastelBlue2
import com.example.loop_new.ui.theme.PastelMint
import com.example.loop_new.ui.theme.PastelMint1
import com.example.loop_new.ui.theme.PastelMint2
import com.example.loop_new.ui.theme.PastelPurple
import com.example.loop_new.ui.theme.PastelPurple1
import com.example.loop_new.ui.theme.PastelPurple2
import com.example.loop_new.ui.theme.PastelRose
import com.example.loop_new.ui.theme.PastelRose1
import com.example.loop_new.ui.theme.PastelRose2
import com.example.loop_new.ui.theme.PastelWhite
import com.example.loop_new.ui.theme.PastelWhite1
import com.example.loop_new.ui.theme.PastelWhite2
import com.example.loop_new.ui.theme.PastelYellow
import com.example.loop_new.ui.theme.PastelYellow1
import com.example.loop_new.ui.theme.PastelYellow2
import com.example.loop_new.ui.theme.White

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrivateScreenPreview() {
    val navController = rememberNavController()
    val sampleData = privateCreateSampleData()

//    Screen(navController, sampleData) { _, _, _ -> }
}

@Composable
fun privateCreateSampleData(): List<Box> {
    val sampleData = mutableListOf<Box>()

    for (i in 1..22) {
        sampleData.add(Box("Box $i", "Description $i"))
    }
    return sampleData
}

// UI
@Composable
fun PrivateBoxScreen(
    navController: NavController,
    viewModel: PrivateBoxViewModel,
) {

    PrivateScreen(
        navController = navController,
        list = viewModel.privateBoxList,
        viewModel,
    )
    { nameInput, describeInput, groupColor ->
        viewModel.createBoxInPrivateSection(nameInput, describeInput, groupColor)
    }
}

@Composable
fun PrivateScreen(
    navController: NavController,
    list: List<Box>,
    viewModel: PrivateBoxViewModel,
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
            bottom.linkTo(parent.bottom, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
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
            .padding(bottom = 42.dp)
    ) {

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("boxList"),
            columns = GridCells.Fixed(itemsInRow)
        ) {
            itemsIndexed(list) { index, box ->
                BoxItem(box) { boxUid ->
                    navController.navigate("${NavigationSupport.PrivateFlashcardScreen}/$boxUid/${box.name}")
                }
                // Sprawdzenie, czy osiągnięto koniec listy i załadowanie więcej boxów
                if (index == list.size - 1 && viewModel.canLoadMore) {
                    LaunchedEffect(Unit) {
                        viewModel.loadMorePrivateBoxes()
                    }
                }
            }

            if (!viewModel.hasMoreData.value) {
                item {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.baseline_add_circle_box),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("addBoxButton")
                .size(60.dp)
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
    showDialog: Boolean,
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

                    // Snackbar container
                    SnackbarHost(hostState = snackbarHostState)
                }
            }
        }
    }
}