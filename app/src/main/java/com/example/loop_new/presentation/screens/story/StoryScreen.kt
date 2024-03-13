package com.example.loop_new.presentation.screens.story

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.Black2
import com.example.loop_new.ui.theme.Gray
import com.example.loop_new.ui.theme.White

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun StoryScreenPreview() {

//    StoryScreen(emptyList())
}

@Composable
fun StoryScreen(navController: NavController, viewModel: StoryViewModel) {

    BackHandler { /* gesture return is off */ }

    val categoryList by viewModel.categoryList.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        categoryList.forEach { category ->
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${category.name}:",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .width(82.dp)
                            .padding(8.dp)
                            .border(0.25.dp, Gray, RoundedCornerShape(6.dp))
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 2.dp),
                            text = "Check All >",
                            fontSize = 12.sp,
                            color = Gray
                        )
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    category.stories.forEach { story ->
                        item {
                            story.favorite?.let {
                                StoryItem(
                                    story = story,
                                    favorite = story.favoriteStories?.any { it.favorite == true }
                                        ?: false) {
                                    navController.navigate("${NavigationSupport.StoryInfoScreen}/${story.uid}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryItem(story: Story, favorite: Boolean, onClick: () -> Unit) {

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

        Box {
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

            val image = if (favorite) {
                painterResource(id = R.drawable.star_gold)
            } else {
                null
            }

            if (image != null) {
                Image(
                    painter = image,
                    contentDescription = "favorite",
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                )
            }
        }

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