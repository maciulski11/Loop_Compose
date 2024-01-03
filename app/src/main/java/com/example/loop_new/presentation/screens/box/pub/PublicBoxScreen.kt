package com.example.loop_new.presentation.screens.box.pub

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.presentation.screens.box.AnimatedLearningButton
import com.example.loop_new.presentation.screens.box.BoxItem

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
        sampleData.add(Box("Box $i", "Description $i"))
    }
    return sampleData
}

// UI
@Composable
fun PublicBoxScreen(navController: NavController, viewModel: PublicBoxViewModel) {

    PublicScreen(
        navController = navController,
        list = viewModel.publicBoxList,
        viewModel
    )
}

@Composable
fun PublicScreen(
    navController: NavController,
    list: List<Box>,
    viewModel: PublicBoxViewModel,
) {

    val showDialogDeleteBox = remember { mutableStateOf(false) }

    BackHandler { /* gesture return is off */ }

    val constraints = ConstraintSet {
        val boxList = createRefFor("boxList")
        val repeatButton = createRefFor("repeatButton")

        constrain(boxList) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
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
                BoxItem(box, {
                    navController
                        .navigate("${NavigationSupport.FlashcardScreen}/${box.uid}/${box.name}")
                })
                { showDialogDeleteBox.value = false }

                // Sprawdzenie, czy osiągnięto koniec listy i załadowanie więcej boxów
                if (index == list.size - 1 && viewModel.canLoadMore) {
                    LaunchedEffect(Unit) {
                        viewModel.loadMoreBoxes()
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

        if (!viewModel.isListEmpty) {
            AnimatedLearningButton(
                onClick = {
                    navController.navigate(NavigationSupport.RepeatScreen)
                }
            )
        }
    }
}