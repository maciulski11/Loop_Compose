package com.example.loop_new.presentation.screens.story_section.story

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.screens.story_section.StoryItem
import com.example.loop_new.ui.theme.Gray

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
                            .clickable {

                            }
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 2.dp).clickable {
                                navController
                                    .navigate("${NavigationSupport.StoryCheckAllScreen}/${category.name}")
                            },
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
                                    navController
                                        .navigate("${NavigationSupport.StoryInfoScreen}/${story.uid}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}