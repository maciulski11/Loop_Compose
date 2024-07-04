package com.example.loop_new.presentation.screens.story_section.story_favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Favorite
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.presentation.screens.dialogs.DeleteFavoriteStoryDialog
import com.example.loop_new.ui.theme.Black2
import com.example.loop_new.ui.theme.Gray
import com.example.loop_new.ui.theme.White
import com.example.loop_new.ui.theme.colorA1
import com.example.loop_new.ui.theme.colorA2
import com.example.loop_new.ui.theme.colorB1
import com.example.loop_new.ui.theme.colorB2
import com.example.loop_new.ui.theme.colorC1
import com.example.loop_new.ui.theme.colorC2

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
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your favorites list is empty...",
                style = MaterialTheme.typography.h6,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
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
                FavoriteStoryItem(
                    story = story,
                    onClick = {
                        navController
                            .navigate("${NavigationSupport.StoryInfoScreen}/${story.uid}/room")
                    },
                    onDelete = {
                        viewModel.deleteStoryWithChapters(story.id)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteStoryItem(story: Favorite, onClick: () -> Unit, onDelete: () -> Unit) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .height(196.dp)
            .width(122.dp)
            .padding(6.dp)
            .background(White, shape = RoundedCornerShape(4.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDialog = true
                    },
                    onTap = {
                        onClick()
                    }
                )
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

            val image = if (story.favorite == true) {
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

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                White,
                                when (story.level) {
                                    "a1" -> colorA1
                                    "a2" -> colorA2
                                    "b1" -> colorB1
                                    "b2" -> colorB2
                                    "c1" -> colorC1
                                    "c2" -> colorC2
                                    else -> White
                                }
                            ),
                            startX = 0f,
                            endX = 100f
                        ),
                        shape = CircleShape
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                val levelText = when (story.level) {
                    "a1" -> "A1"
                    "a2" -> "A2"
                    "b1" -> "B1"
                    "b2" -> "B2"
                    "c1" -> "C1"
                    "c2" -> "C2"
                    else -> ""
                }

                Text(
                    text = levelText,
                    fontSize = 10.sp,
                    color = Color.Black
                )
            }
        }

        val fieldWidth = 32

        (story.title?.let { title ->
            if (title.length > fieldWidth) {
                val lastSpaceIndex = title.lastIndexOf(" ", fieldWidth - 3)
                if (lastSpaceIndex != -1) {
                    "${title.substring(0, lastSpaceIndex)}..."
                } else {
                    "${title.substring(0, fieldWidth - 3)}..."
                }
            } else {
                title
            }
        }).let { truncatedTitle ->

            Text(
                text = truncatedTitle ?: "",
                color = Black2,
                fontWeight = FontWeight.Bold,
                minLines = 2,
                maxLines = 2,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }

    if (showDialog) {
        DeleteFavoriteStoryDialog(
            story = story,
            onDelete = {
                onDelete()
                showDialog = false
            },
            onClose = {
                showDialog = false
            }
        )
    }
}
