package com.example.loop_new.presentation.screens.story_section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.ui.theme.Black2
import com.example.loop_new.ui.theme.White
import com.example.loop_new.ui.theme.colorA1
import com.example.loop_new.ui.theme.colorA2
import com.example.loop_new.ui.theme.colorB1
import com.example.loop_new.ui.theme.colorB2
import com.example.loop_new.ui.theme.colorC1
import com.example.loop_new.ui.theme.colorC2

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

            //TODO: sprawdizc dzialanie w firabse czy sie aktualizuje
//            val image = if (favorite) {
//                painterResource(id = R.drawable.star_gold)
//            } else {
//                null
//            }
//
//            if (image != null) {
//                Image(
//                    painter = image,
//                    contentDescription = "favorite",
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .align(Alignment.BottomEnd)
//                        .size(20.dp)
//                )
//            }

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
}