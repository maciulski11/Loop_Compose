package com.example.loop_new.presentation.screens.box

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    val navController = rememberNavController()
    val sampleData = createSampleData()

//    Screen(navController, sampleData) { _, _, _ -> }
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
    )
}

@Composable
fun Screen(
    navController: NavController,
    list: List<Box>,
    viewModel: BoxViewModel,
) {

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
            items(list) { box ->
                BoxItem(box) { boxUid ->
                    navController.navigate("${NavigationSupport.FlashcardScreen}/$boxUid/${box.name}")
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