package com.example.loop_new.presentation.screens.story_section.story_favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.Black2
import com.example.loop_new.ui.theme.Gray
import com.example.loop_new.ui.theme.White

@Preview
@Composable
fun StoryFavoriteScreenPreview() {
//    StoryFavoriteScreen()
}

@Composable
fun StoryFavoriteScreen(navController: NavController, viewModel: StoryFavoriteViewModel) {

    val stories by remember { viewModel.stories }.collectAsState(emptyList())

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
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(stories) { story ->
            StoryItem(story) {
                navController.navigate("${NavigationSupport.ReadScreen}/${story.uid}")
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryItem(story: Story, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .height(196.dp)
            .width(122.dp)
            .padding(6.dp)
            .background(White, shape = RoundedCornerShape(4.dp))
            .clickable {
                onClick()
            }
    ) {

        GlideImage(
            model = story.image ?: "",
            contentDescription = "loadImage",
            modifier = Modifier
                .height(150.dp)
                .width(122.dp)
                .shadow(1.25.dp, RoundedCornerShape(4.dp))
                .padding(0.5.dp)
                .clip(shape = RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = story.title.toString(),
            color = Black2,
            fontWeight = FontWeight.Bold,
            minLines = 2,
            maxLines = 2,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}