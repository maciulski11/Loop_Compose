package com.example.loop_new.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.domain.model.Box
import com.example.loop_new.NavigationScreens
import com.example.loop_new.R
import kotlinx.coroutines.flow.Flow

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    val navController = rememberNavController()
    val exampleData = createSampleData()

    Screen(navController, exampleData) { _, _ -> }
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
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    Screen(
        navController = navController,
        list = viewModel.boxList.value ?: emptyList()
    ) { nameInput, describeInput ->
        viewModel.addBox(nameInput, describeInput)
    }
}

@Composable
fun Screen(
    navController: NavController,
    list: List<Box>,
    onAddBox: (nameInput: String, describeInput: String) -> Unit
) {
    val showDialogState = remember { mutableStateOf(false) }

    val constraints = ConstraintSet {
        val boxList = createRefFor("boxList")
        val addBoxButton = createRefFor("addBoxButton")

        constrain(boxList) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(addBoxButton) {
            bottom.linkTo(parent.bottom, margin = 14.dp)
            end.linkTo(parent.end, margin = 16.dp)
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
                BoxItem(box = box, navController)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.baseline_add_circle_box),
            contentDescription = "Button",
            modifier = Modifier
                .layoutId("addBoxButton")
                .clickable {
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
    }
}

@Composable
fun ShowCustomAlertDialog(
    onAddBox: (nameInput: String, describeInput: String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf("") }
    var describeInput by remember { mutableStateOf("") }


    AlertDialog(
        modifier = Modifier
            .height(300.dp)
            .width(300.dp)
            .background(Color.White),
        onDismissRequest = { onDismiss() },
        title = {
            Text("Create Box:")
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
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        onAddBox(nameInput, describeInput)
                        onDismiss()
                    }
                ) {
                    Text("OK")
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
            .clickable {
                navController.navigate(NavigationScreens.BoxScreen)
            },
        contentAlignment = Alignment.Center,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, Color.Black, RoundedCornerShape(20.dp)),
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


