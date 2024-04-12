package com.example.loop_new.presentation.screens.flashcard_section.box.pub

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.R
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.presentation.screens.flashcard_section.box.AnimatedLearningButton
import com.example.loop_new.presentation.screens.flashcard_section.box.BoxItem

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PublicScreenPreview() {
    val navController = rememberNavController()
    val sampleData = publicCreateSampleData()

//    Screen(navController, sampleData) { _, _, _ -> }
}

@Composable
fun publicCreateSampleData(): List<Box> {
    val sampleData = mutableListOf<Box>()

    for (i in 1..22) {
//        sampleData.add(Box("Box $i", "Description $i", 0, true ))
    }
    return sampleData
}

// UI
@Composable
fun PublicBoxScreen(navController: NavController, viewModel: PublicBoxViewModel) {

    PublicScreen(
        navController = navController,
        list = viewModel.publicBoxList,
        viewModel = viewModel
    )
}

@Composable
fun PublicScreen(
    navController: NavController,
    list: List<Box>,
    viewModel: PublicBoxViewModel,
) {
    BackHandler { navController.navigate(NavigationSupport.PrivateBoxScreen) }

    val showDialogDeleteBox = remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val constraints = ConstraintSet {
        val boxList = createRefFor("boxList")
        val repeatButton = createRefFor("repeatButton")
        val background = createRefFor("background")

        constrain(boxList) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(background) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(repeatButton) {
            bottom.linkTo(parent.bottom, margin = 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    ConstraintLayout(
        constraints,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.light_blue_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("boxList")
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.find),
                        contentDescription = null,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                        ),
                        placeholder = {
                            Text(
                                text = "search...",
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                    )
                }
            }

            // Filters the list based on the values entered into the search field
            val filteredList = if (searchText.isNotEmpty()) {
                list.filter { it.name!!.contains(searchText, ignoreCase = true) }
            } else {
                list
            }

            itemsIndexed(filteredList) { index, box ->
                BoxItem(box,
                    onClick = {
                        navController.navigate("${NavigationSupport.FlashcardScreen}/${box.uid}/${box.name}")
                    })
                { showDialogDeleteBox.value = false }

                // Checking if the end of the list has been reached and loading more boxes
                if (index == list.size - 1 && viewModel.canLoadMore) {
                    LaunchedEffect(Unit) {
                        viewModel.loadMoreBoxes()
                    }
                }
            }
        }

        // Repeat button
        if (!viewModel.isListEmpty) {
            AnimatedLearningButton(
                onClick = {
                    navController.navigate(NavigationSupport.RepeatScreen)
                }
            )
        }
    }
}