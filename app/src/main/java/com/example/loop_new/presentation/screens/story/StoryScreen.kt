package com.example.loop_new.presentation.screens.story

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.presentation.navigation.NavigationSupport

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun StoryScreenPreview() {

//    StoryScreen(emptyList())
}

@Composable
fun StoryScreen(navController: NavController, viewModel: StoryViewModel) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        items(viewModel.storyList) { story ->
            StoryItem(story = story) {

                navController.navigate("${NavigationSupport.ReadScreen}/${story.uid}")

            }
        }

    }
}

@Composable
fun StoryItem(story: Story, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick()
            }
    ) {

        Text(text = story.title.toString())
    }
}