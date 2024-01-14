package com.example.loop_new.presentation.screens.read

import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loop_new.R

data class WordWithIndices(val word: String, val start: Int, val end: Int)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReadScreen() {
    var selectedText by remember { mutableStateOf<String?>(null) }
    var selectedOffset by remember { mutableStateOf<Int?>(null) }
    var clickPosition by remember { mutableStateOf(Offset(0f, 0f)) }

    val text = """
    Nie jesteś Słońcem, wokół którego chciałabym krążyć. Jesteś pieprzoną planetą 
    w najdalszym zakątku tego wszechświata, a moje życie jest poświęcone misji, 
    aby ją zbadać.

    Kiedy patrzę w górę na nocne niebo, widzę Twoją obecność wśród gwiazd. 
    Jesteś jedną z nich, błyszczącą i oddaloną. Czasami wydaje się, że jesteś 
    bardzo daleko, ale moje serce zawsze jest z Tobą.

    Twoja orbita przecina moje myśli, a Twoja grawitacja przyciąga moje marzenia. 
    Chociaż jesteśmy oddzieleni przez ogromną przestrzeń, nasza więź jest silniejsza 
    niż każda siła grawitacyjna.

    Każde spojrzenie w Twoją stronę to podróż przez kosmiczne zakamarki uczuć. 
    Czasem czuję się jak podróżnik między galaktykami, szukający odpowiedzi na pytania 
    o naszą egzystencję.

    Tak, nie jesteś Słońcem, ale dla mnie jesteś centrum tego wszechświata. 
    Twoja obecność nadaje sens mojemu życiu, a Twoje światło rozświetla moją duszę.

    Jesteśmy jak dwie gwiazdy na niebie, które mimo odległości łączą swoje światła. 
    Niech nasza historia trwa wiecznie, choćbyśmy byli oddzieleni przez miliardy lat świetlnych.
"""

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
            append(text)
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .pointerInteropFilter { event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    clickPosition = if (event.x >= 930) {
                        Offset(event.x - 100, event.y)
                    } else if (event.x <= 150) {
                        Offset(event.x, event.y)
                    } else {
                        Offset(event.x - 50, event.y)
                    }
                }
                false
            },
        style = TextStyle(
            color = Color.White,
            textDecoration = TextDecoration.None
        ),
        onClick = { offset ->
            val wordWithIndices = findWordAtIndex(text, offset)
            selectedText = wordWithIndices.word
            selectedOffset = offset
        }
    )

    // Display the custom info box if selectedText is not null
    if (!selectedText.isNullOrBlank() && selectedOffset != null) {
        CustomInfoBox(
            selectedText = selectedText?.lowercase() ?: "",
            clickPosition = clickPosition,
            onDismiss = {
                selectedText = null
                selectedOffset = null
            }
        )
    }
}

@Composable
fun CustomInfoBox(selectedText: String, clickPosition: Offset, onDismiss: () -> Unit) {
    val context = LocalContext.current

    val position = with(LocalDensity.current) {
        Offset(
            x = clickPosition.x / density,
            y = (clickPosition.y / density) + 32
        )
    }

    Box(
        modifier = Modifier
            .offset(position.x.dp, position.y.dp)
            .background(Color.Gray, RoundedCornerShape(10.dp))
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                // You can add custom actions when the info box is clicked
                onDismiss.invoke()
            }
    ) {

        Row {
            // Your content inside the info box goes here
            Text(text = selectedText, color = Color.White)

            Spacer(modifier = Modifier.width(4.dp))

            Image(
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onDismiss.invoke()
                        Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
                    },
                painter = painterResource(id = R.drawable.baseline_add_circle_box),
                contentDescription = "add"
            )
        }
    }
}

fun findWordAtIndex(text: String, offset: Int): WordWithIndices {
    var startIndex = offset
    var endIndex = offset

    // Find the start of the word
    while (startIndex > 0 && text[startIndex - 1] != ' ') {
        startIndex--
    }

    // Find the end of the word
    while (endIndex < text.length && text[endIndex] != ' ') {
        endIndex++
    }

    // Get the word
    val word = text.substring(startIndex, endIndex)

    // Filter out punctuation
    val filteredWord = word.filter { it.isLetterOrDigit() }

    return WordWithIndices(filteredWord, startIndex, endIndex)
}