package com.example.loop_new.presentation.screens.story_section.story_favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.screens.animations.ProgressLoading
import com.example.loop_new.presentation.screens.story_section.StoryItem
import com.example.loop_new.ui.theme.Gray

@Preview
@Composable
fun StoryFavoriteScreenPreview() {
//    StoryFavoriteScreen()
}

@Composable
fun StoryFavoriteScreen(navController: NavController, viewModel: StoryFavoriteViewModel) {

    val stories by remember { viewModel.stories }.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {

        ProgressLoading()

    } else {

        if (stories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "favorites list is empty...",
                    fontSize = 22.sp,
                    color = Gray
                )
            }
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize(), contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(stories) { story ->
                    story.favoriteStories?.any { it.favorite == true }?.let {
                        StoryItem(
                            story,
                            favorite = it
                        ) {
                            navController
                                .navigate("${NavigationSupport.StoryInfoScreen}/${story.uid}")
                        }
                    }
                }
            }
        }
    }
}
