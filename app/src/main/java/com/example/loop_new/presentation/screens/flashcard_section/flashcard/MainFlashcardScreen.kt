package com.example.loop_new.presentation.screens.flashcard_section.flashcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.KnowledgeLevel
import com.example.loop_new.ui.theme.Black
import com.example.loop_new.ui.theme.Blue
import com.example.loop_new.ui.theme.Green
import com.example.loop_new.ui.theme.Orange
import com.example.loop_new.ui.theme.Red

@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    onPlayAudioFromUrl: (String) -> Unit,
    onLongPress: (Boolean) -> Unit,
    onClick: (Int) -> Unit
) {
    val color = remember { mutableStateOf(Black) } // Domyślny kolor

    when (flashcard.knowledgeLevel) {
        KnowledgeLevel.KNOW.value -> color.value = Green
        KnowledgeLevel.DO_NOT_KNOW.value -> color.value = Red
        KnowledgeLevel.SOMEWHAT_KNOW.value -> color.value = Blue
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 6.dp,
                start = 2.dp,
                end = 2.dp
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {onClick(flashcard.idFlashcard)},
                    onLongPress = { onLongPress(true) }
                )
            },
        contentAlignment = Alignment.Center,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, color.value, RoundedCornerShape(20.dp)),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = flashcard.word.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 2.dp)
                    )

                    Image(
                        painter = painterResource(

                            id = if (flashcard.audioUrl!!.isNotEmpty()) {
                                R.drawable.baseline_volume
                            } else {
                                R.drawable.baseline_volume_off
                            }
                        ),
                        contentDescription = "Button",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 2.dp, start = 4.dp)
                            .align(Alignment.CenterVertically)
                            .size(32.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (flashcard.audioUrl.isNotEmpty()) {
                                    onPlayAudioFromUrl(flashcard.audioUrl)
                                }
                            }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                }

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = flashcard.pronunciation.toString(),
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center,
                    color = Orange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 14.dp)
                )
            }
        }
    )
}