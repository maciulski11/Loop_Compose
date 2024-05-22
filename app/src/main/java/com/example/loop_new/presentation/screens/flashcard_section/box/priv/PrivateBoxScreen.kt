package com.example.loop_new.presentation.screens.flashcard_section.box.priv

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.example.loop_new.presentation.screens.flashcard_section.box.AnimatedLearningButton
import com.example.loop_new.presentation.screens.flashcard_section.box.BoxItem
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
import com.example.loop_new.ui.theme.Red
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
//        sampleData.add(Box("Box $i", "Description $i"))
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
        viewModel
    ) { nameInput, describeInput, groupColor ->
        viewModel.insert(nameInput, describeInput, groupColor)
    }
}

@Composable
fun PrivateScreen(
    navController: NavController,
    viewModel: PrivateBoxViewModel,
    onAddBox: (nameInput: String, describeInput: String, colors: List<Color>) -> Unit,
) {
    BackHandler { /* gesture return is off */ }

    val boxes by viewModel.boxes.collectAsState(emptyList())

    val showDialogCreateBox = remember { mutableStateOf(false) }
    val showDialogDeleteBox = remember { mutableStateOf(false) }
    val selectedBoxIndex = remember { mutableIntStateOf(-1) }

    val constraints = ConstraintSet {
        val boxList = createRefFor("boxList")
        val addBoxButton = createRefFor("addBoxButton")
        val repeatButton = createRefFor("repeatButton")
        val background = createRefFor("background")

        constrain(background) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

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

    ConstraintLayout(
        constraints, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 42.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.light_blue_background),
            contentDescription = "background", // zastąp opisem, jeśli to konieczne
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // lub inny ContentScale według potrzeb
        )

        if (boxes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = "Welcome to Your Private Section!",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Gray
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 46.dp),
                        text = "If you want to start learning you have 2 options:\n" +
                                "1. You can return to public section and find you favourite box with flashcard.\n" +
                                "2. You can create your box and flashcards with your preferences.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Gray
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("boxList"),
        ) {

            itemsIndexed(boxes) { index, box ->
                BoxItem(box, {
                    navController
                        .navigate("${NavigationSupport.PrivateFlashcardScreen}/${box.uid}/${box.id}/${box.name}")
                }) {

                    selectedBoxIndex.intValue = index // Ustawienie indeksu wybranego pudełka
                    showDialogDeleteBox.value = true
                }
            }
        }

        Image(painter = painterResource(id = R.drawable.baseline_add_circle_box),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("addBoxButton")
                .size(60.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showDialogCreateBox.value = true
                })

        if (showDialogCreateBox.value) {
            ShowCreateBoxAlertDialog(
                { nameInput, describeInput, groupColor ->
                    onAddBox(nameInput, describeInput, groupColor)
                },
                {
                    showDialogCreateBox.value = false
                },
                showDialogCreateBox.value
            )
        }

        if (showDialogDeleteBox.value) {
            val selectedBox = if (selectedBoxIndex.value != -1) boxes[selectedBoxIndex.value] else null
            selectedBox?.let { box ->
                ShowDeleteBoxAlertDialog(
                    boxName = box.name.toString(),
                    showDialog = showDialogDeleteBox.value,
                    onDelete = {
                        viewModel.delete(box.uid.toString()) // Usuwanie pudełka
                    },
                    onDismiss = {
                        showDialogDeleteBox.value = false
                    }
                )
            }
        }

        if (!viewModel.isListEmpty) {
            AnimatedLearningButton(onClick = {
                navController.navigate(NavigationSupport.RepeatScreen)
            })
        }
    }
}

@Composable
fun ShowDeleteBoxAlertDialog(
    boxName: String,
    showDialog: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {

    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .width(270.dp)
                    .heightIn(min = 100.dp, max = 200.dp)
                    .background(White, shape = RoundedCornerShape(20.dp))
                    .border(3.dp, Black, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Column {

                    val annotatedString = buildAnnotatedString {
                        append("Do you want to delete box: ")

                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(boxName)
                        }

                        append("?")
                    }

                    Text(text = annotatedString, fontSize = 22.sp)


                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {

                        Button(modifier = Modifier
                            .height(40.dp)
                            .width(84.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                            onClick = {
                                onDismiss()
                            }) {
                            Text(
                                text = "Cancel",
                                color = Black
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Button(modifier = Modifier
                            .height(40.dp)
                            .width(84.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                            onClick = {
                                onDelete()
                                onDismiss()
                            }) {
                            Text(
                                text = "OK",
                                color = Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun ShowCreateBoxAlertDialog(
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

            SnackbarMessage.None -> { /* Don't do anything */ }
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
                        text = "Create Box:", fontWeight = FontWeight.Bold, fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(colorGroups) { group ->
                            val representativeColor = group.first()
                            Box(modifier = Modifier
                                .size(40.dp)
                                .background(representativeColor, shape = CircleShape)
                                .border(1.dp, Black, shape = CircleShape)
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    backgroundColor = representativeColor
                                    selectedColorGroup = group
                                })
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(value = nameInput, maxLines = 2, onValueChange = {
                        if (it.length <= 35) {
                            nameInput = it
                        } else {
                            snackbarMessage = SnackbarMessage.MaxCharactersExceededName
                        }
                    }, keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Go,
                    ), placeholder = { Text("Box Name - 35 characters") })

                    // Licznik znaków jako adnotacja
                    Text(
                        text = "${nameInput.length}/${35}",
                        modifier = Modifier
                            .padding(top = 4.dp, end = 12.dp)
                            .align(Alignment.End),
                        style = TextStyle(color = Gray),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(value = describeInput, maxLines = 3, onValueChange = {
                        if (it.length <= 60) {
                            describeInput = it
                        } else {
                            snackbarMessage = SnackbarMessage.MaxCharactersExceededDescription
                        }
                    }, keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Go,
                    ), placeholder = { Text("Description - 60 characters") })

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

                        Button(modifier = Modifier
                            .height(40.dp)
                            .width(84.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                            onClick = {
                                onDismiss()
                            }) {
                            Text(
                                text = "Cancel", color = White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Button(modifier = Modifier
                            .height(40.dp)
                            .width(84.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                            onClick = {
                                onAddBox(nameInput, describeInput, selectedColorGroup)
                                onDismiss()
                            }) {
                            Text(
                                text = "OK", color = Black
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

enum class SnackbarMessage {
    None, MaxCharactersExceededName, MaxCharactersExceededDescription
}