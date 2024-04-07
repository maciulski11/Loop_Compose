package com.example.loop_new.presentation.screens.story_section.story_check_all

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.screens.story_section.StoryItem

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoryCheckAllScreenPreview() {
//    StoryCheckAllScreen()
}

@Composable
fun StoryCheckAllScreen(
    navController: NavController,
    viewModel: StoryCheckAllViewModel,
    category: String,
) {

    val stories by remember { viewModel.stories }.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Text(
            text = "${category}:",
            fontSize = 20.sp,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(bottom = 6.dp, start = 16.dp, top = 16.dp)

        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize(), contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(stories) { story ->
                StoryItem(
                    story,
                    favorite = story.favoriteStories?.any { it.favorite == true } ?: false
                ) {
                    navController.navigate("${NavigationSupport.StoryInfoScreen}/${story.uid}")
                }
            }
        }
    }
}