package com.example.loop_new.presentation.screens.read

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.R
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.presentation.navigation.NavigationSupport
import com.example.loop_new.ui.theme.Gray2

data class WordWithIndices(val word: String, val start: Int, val end: Int)

@Preview(showBackground = true)
@Composable
fun StoryScreenPreview() {
//    ReadScreen()
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReadScreen(navController: NavController, viewModel: ReadViewModel) {

    // Support for custom return behavior
    BackHandler {
        // Where return
        navController.navigate(NavigationSupport.StoryScreen)
    }

    var selectedText by remember { mutableStateOf<String?>(null) }
    var selectedOffset by remember { mutableStateOf<Int?>(null) }
    var clickPosition by remember { mutableStateOf(Offset(0f, 0f)) }

    val charLimit = 2000

    var currentPage by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    if (viewModel.storyDetails != null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {

            Text(
                text = viewModel.storyDetails?.text?.getOrNull(currentPage)?.chapter ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                ClickableText(
                    text = createAnnotatedStringForPage(currentPage, viewModel.storyDetails),
                    modifier = Modifier
                        .padding(bottom = 14.dp, start = 12.dp, end = 12.dp)
                        .pointerInteropFilter { event ->
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                clickPosition = if (event.x >= 930) {
                                    Offset(event.x - 100, event.y)
                                } else if (event.x <= 150) {
                                    Offset(event.x, event.y)
                                } else {
                                    Offset(event.x - 50, event.y)
                                }

                                viewModel.translate = ""
                                selectedText = null
                                selectedOffset = null
                            }
                            false
                        },
                    style = TextStyle(
                        color = Black,
                        textDecoration = TextDecoration.None
                    ),
                    onClick = { offset ->
                        val wordWithIndices = findWordAtIndex(
                            viewModel.storyDetails?.text?.getOrNull(currentPage)?.content ?: "",
                            offset
                        )
                        { clickedWord ->
                            viewModel.translateWord(clickedWord)
                        }

                        selectedText = wordWithIndices.word
                        selectedOffset = offset
                    }
                )

                // Display the custom info box if selectedText is not null
                if (!selectedText.isNullOrBlank() && selectedOffset != null) {
                    CustomInfoBox(
                        selectedText = selectedText?.lowercase() ?: "",
                        translatedWord = viewModel.translate.lowercase(),
                        clickPosition = clickPosition,
                        onDismiss = {
                            selectedText = null
                            selectedOffset = null
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, bottom = 22.dp, start = 56.dp, end = 56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_left_44),
                    contentDescription = "left",
                    modifier = Modifier
                        .alpha(if (currentPage > 0) 1f else 0.5f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (currentPage > 0) {
                                currentPage--
                            }
                        }
                )

                Text(
                    fontSize = 22.sp,
                    text = "${currentPage + 1}/${viewModel.storyDetails?.text?.size ?: 0}"
                )

                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_44),
                    contentDescription = "right",
                    modifier = Modifier
                        .alpha(
                            if (currentPage < (viewModel.storyDetails?.text?.size
                                    ?: 0) - 1
                            ) 1f else 0.5f
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (currentPage < (viewModel.storyDetails?.text?.size ?: 0) - 1) {
                                currentPage++
                            }
                        }
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

    LaunchedEffect(scrollState, currentPage) {
        scrollState.animateScrollTo(0)
    }
}

@Composable
fun createAnnotatedStringForPage(page: Int, story: Story?): AnnotatedString {

    // Sprawdź, czy story nie jest nullem i czy posiada odpowiednie dane
    if (story?.text != null) {
        // Sprawdź, czy currentPage mieści się w zakresie dostępnych rozdziałów
        if (page >= 0 && page < story.text.size) {
            val content = story.text[page].content ?: ""

            // Stwórz AnnotatedString z różnymi stylami dla chapter i content
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                ) {
                    append(content)
                }
            }
            return annotatedString
        }
    }
    // Zwróć pusty AnnotatedString, jeśli nie ma odpowiednich danych
    return AnnotatedString("")
}

@Composable
fun CustomInfoBox(
    selectedText: String,
    translatedWord: String,
    clickPosition: Offset,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    val position = with(LocalDensity.current) {
        Offset(
            x = clickPosition.x / density,
            y = (clickPosition.y / density) + 15
        )
    }

    Box(
        modifier = Modifier
            .offset(position.x.dp, position.y.dp)
            .background(Gray2, RoundedCornerShape(10.dp))
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                // You can add custom actions when the info box is clicked
                onDismiss.invoke()
            }
    ) {
        Row {

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onDismiss.invoke()
                        Toast
                            .makeText(context, "Successfully added!", Toast.LENGTH_SHORT)
                            .show()
                    },
                painter = painterResource(id = R.drawable.baseline_add_circle_box),
                contentDescription = "add"
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                // Your content inside the info box goes here
                Text(text = selectedText, color = Color.White)

                Text(text = translatedWord, color = Color.White)
            }
        }
    }
}

fun findWordAtIndex(text: String, offset: Int, translateWord: (String) -> Unit): WordWithIndices {
    var startIndex = offset
    var endIndex = offset

    // Znajdź początek słowa, uwzględniając znaki interpunkcyjne
    while (startIndex > 0 && !text[startIndex - 1].isWhitespace()) {
        startIndex--
    }

    // Znajdź koniec słowa, uwzględniając znaki interpunkcyjne
    while (endIndex < text.length && !text[endIndex].isWhitespace()) {
        endIndex++
    }

    // Pobierz słowo
    val word = text.substring(startIndex, endIndex)

    // Filtruj znaki interpunkcyjne z końca słowa
    val filteredWord = word.trimEnd { it.isWhitespace() || it.isPunctuation() }

    translateWord(filteredWord)

    return WordWithIndices(filteredWord, startIndex, endIndex)
}

// Rozszerzenie do Char na sprawdzenie, czy jest znakiem interpunkcyjnym
fun Char.isPunctuation(): Boolean {
    return this in setOf(',', '.', ';', ':', '!', '?', '#', '*')
}