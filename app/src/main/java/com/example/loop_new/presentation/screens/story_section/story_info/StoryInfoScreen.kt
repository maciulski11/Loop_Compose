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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.example.loop_new.presentation.screens.animations.ProgressLoading
import com.example.loop_new.ui.theme.BlueOfBackgroundApp
import com.example.loop_new.ui.theme.White
import com.example.loop_new.ui.theme.blueApp
import com.example.loop_new.ui.theme.greenApp
import java.util.Locale

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoryInfoScreenPreview() {
//    StoryInfoScreen()
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoryInfoScreen(navController: NavController, viewModel: StoryInfoViewModel, storyUid: String) {

    if (viewModel.storyDetails == null && viewModel.favoriteDetails == null) {

        ProgressLoading()

    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GlideImage(
                model = if (viewModel.favoriteDetails != null) {
                    viewModel.favoriteDetails?.image
                } else {
                    viewModel.storyDetails?.image ?: ""
                },
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
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(greenApp, blueApp)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            2.dp, brush = Brush.verticalGradient(
                                colors = listOf(greenApp, blueApp)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    (if (viewModel.favoriteDetails != null) {
                        viewModel.favoriteDetails?.title
                    } else {
                        viewModel.storyDetails?.title ?: ""
                    })?.let {
                        Text(
                            text = it,
                            fontSize = 26.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(2.dp)
                        )
                    }
                }

                Text(
                    text = "author: ${
                        if (viewModel.favoriteDetails != null) {
                            viewModel.favoriteDetails?.author
                        } else {
                            viewModel.storyDetails?.author ?: ""
                        }
                    }",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )

                Text(
                    text = "level: ${
                        if (viewModel.favoriteDetails != null) {
                            viewModel.favoriteDetails?.level?.toUpperCase(Locale.ROOT)
                        } else {
                            viewModel.storyDetails?.level?.toUpperCase(Locale.ROOT) ?: ""
                        }
                    }",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp)
                )

                if (viewModel.storyDetails != null) {
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
                                text = viewModel.storyDetails?.viewList?.size.toString(),
                                color = White,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                (if (viewModel.favoriteDetails != null) {
                    viewModel.favoriteDetails?.category
                } else {
                    viewModel.storyDetails?.category ?: ""
                })?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

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
                                .navigate(
                                    "${NavigationSupport.ReadScreen}/${
                                        if (viewModel.favoriteDetails != null) {
                                            viewModel.favoriteDetails?.uid
                                        } else {
                                            viewModel.storyDetails?.uid ?: ""
                                        }
                                    }"
                                )

                            if (viewModel.storyDetails != null) {
                                viewModel.addStoryUidToViewList(viewModel.storyDetails!!.uid.toString())
                                viewModel.addStoryToFavoriteSection(
                                    viewModel.storyDetails!!,
                                    false
                                )
                            }
                        }
                    ) {
                        Text(text = "Read")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    val isFavorite = remember {
                        mutableStateOf(viewModel.favoriteDetails?.favorite ?: false)
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
                                    if (!isFavorite.value) {
                                        // Add to favorite
                                        viewModel.updateFavoriteStatus(storyUid, true)

                                        if (viewModel.storyDetails != null) {
                                            viewModel.addStoryToFavoriteSection(
                                                viewModel.storyDetails!!,
                                                true
                                            )
                                        }
                                    } else {
                                        // Delete from favorite
                                        viewModel.updateFavoriteStatus(storyUid, false)
                                    }
                                }
                                // Update status isFavorite after click
                                isFavorite.value = !isFavorite.value
                            }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                (if (viewModel.favoriteDetails != null) {
                    viewModel.favoriteDetails?.entry
                } else {
                    viewModel.storyDetails?.entry ?: ""
                })?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        fontSize = 16.sp,
                        text = it
                    )
                }
            }
        }
    }
}