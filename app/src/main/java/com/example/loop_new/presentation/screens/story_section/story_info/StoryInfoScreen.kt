package com.example.loop_new.presentation.screens.story_section.story_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.loop_new.R
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.BlueOfBackgroundApp
import com.example.loop_new.ui.theme.White

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoryInfoScreenPreview() {
//    StoryInfoScreen()
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryInfoScreen(navController: NavController, viewModel: StoryInfoViewModel) {

    if (viewModel.storyDetails != null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GlideImage(
                model = viewModel.storyDetails?.image ?: "",
                contentDescription = "loadImage",
                modifier = Modifier
                    .height(276.dp)
                    .width(194.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 18.dp, bottom = 16.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .shadow(1.25.dp, RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                        .background(BlueOfBackgroundApp, RoundedCornerShape(12.dp))
                        .border(2.dp, BlueOfBackgroundApp, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = viewModel.storyDetails?.title.toString(),
                        fontSize = 26.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(2.dp)
                    )
                }

                Text(
                    text = "author of story",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )

                Text(
                    text = viewModel.storyDetails?.level.toString(),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.read_eye_24),
                        contentDescription = "amountOfView",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .background(BlueOfBackgroundApp, RoundedCornerShape(12.dp))
                            .border(2.dp, BlueOfBackgroundApp, RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = "14554",
                            color = White,
                            modifier = Modifier
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = viewModel.storyDetails?.category.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Row(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        modifier = Modifier
                            .width(80.dp)
                            .align(Alignment.CenterVertically)
                            .padding(top = 4.dp),
                        onClick = {
                            navController
                                .navigate("${NavigationSupport.ReadScreen}/${viewModel.storyDetails?.uid}")
                        }
                    ) {
                        Text(text = "Read")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    val isFavorite = remember {
                        mutableStateOf(viewModel.storyDetails?.favoriteStories?.any { it.favorite == true }
                            ?: false)
                    }

                    val image = if (isFavorite.value) {
                        painterResource(id = R.drawable.star_gold)
                    } else {
                        painterResource(id = R.drawable.star)
                    }

                    Image(
                        painter = image,
                        contentDescription = "favorite",
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                viewModel.apply {
                                    if (isFavorite.value) {
                                        // Add to favorite
                                        removeStoryFromFavoriteSection()
                                    } else {
                                        // Delete from favorite
                                        addStoryToFavoriteSection()
                                    }
                                }
                                // Update status isFavorite after click
                                isFavorite.value = !isFavorite.value
                            }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    fontSize = 16.sp,
                    text = "Według osób dotkniętych emailowstrętem, z pomocą spamu można otrzymać też spyware, który otwiera nasz komputer na cały świat i dopisuje do nieformalnej listy wolnych zasobów internetowych (tzw. botnet) albo przestawia układ klawiatury QWERTY na dowolny inny; posiada też możliwość dynamicznej zmiany układu klawiatury, kiedy już – pogodzeni z tym nieszczęściem – za pomocą markera opiszemy klawiaturę od nowa."
                )
            }
        }
    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(66.dp),
                strokeWidth = 7.dp,
            )
        }
    }
}