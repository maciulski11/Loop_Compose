package com.example.loop_new.presentation.screens.story

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    BackHandler { /* gesture return is off */ }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
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
            .height(174.dp)
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.Green, shape = RoundedCornerShape(4.dp))
            .clickable {
                onClick()
            }
    ) {
        Text(text = story.title.toString())
    }
}