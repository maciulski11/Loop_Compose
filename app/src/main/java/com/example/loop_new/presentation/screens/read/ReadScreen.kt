package com.example.loop_new.presentation.screens.read

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class WordWithIndices(val word: String, val start: Int, val end: Int)

@Preview(showBackground = true)
@Composable
fun StoryScreenPreview() {
    ReadScreen()
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReadScreen() {
    val text = """
Chapter 1: The Mysterious Beginning

    In the small town of Ravenwood, where the misty woods met the rolling hills,
    a mysterious event was about to unfold. It all started on a moonlit night,
    with the wind whispering secrets through the ancient trees.

    Sarah, a curious young girl with an insatiable thirst for adventure, found
    an old, dusty book in the attic of her grandmother's house. Little did she
    know that this book held the key to unlocking a hidden realm filled with
            magic and enigma.

    As Sarah began to read the words written in the book, a soft glow surrounded
    her, transporting her to a place where reality blended with fantasy.
    The journey had just begun.

Chapter 2: The Enchanted Realm

    Sarah found herself in an enchanted realm, where talking animals and mystical
    creatures roamed freely. The sky was painted in hues of lavender and indigo,
    and the air was filled with the sweet scent of blooming flowers.

    Guided by a wise old owl named Merlin, Sarah learned about the ancient prophecy
    that foretold her arrival. She was chosen to embark on a quest to restore the
            balance between light and darkness.

Chapter 3: The Forbidden Forest

    The quest led Sarah to the Forbidden Forest, a place where the shadows danced
    among the trees, and whispers of the past echoed in the wind. Deep within the
    forest, she discovered an ancient temple guarded by mystical guardians.

    To unlock the temple's secrets, Sarah had to solve riddles and face her deepest
    fears. Each step brought her closer to unveiling the truth behind the mysterious
            events in Ravenwood.
            
Chapter 4: The Temple's Trials

As Sarah delved deeper into the Forbidden Forest, the trials within the ancient temple tested her resolve. She faced illusions that mirrored her innermost desires and fears, challenging her to confront the shadows of her past.

Guided by the wisdom of Merlin and the strength within her heart, Sarah overcame each trial with newfound courage. The temple unveiled its secrets, revealing an ancient artifact that held the power to restore balance.

Chapter 5: The Dark Prophecy

With the artifact in hand, Sarah learned of a dark prophecy that threatened the very fabric of the enchanted realm. An ancient evil, long thought to be vanquished, sought to rise again and plunge the world into eternal darkness.

Sarah, now accompanied by a fellowship of magical beings she met along her journey, embarked on a quest to thwart the impending doom. Their path led them through mystical lands, across treacherous mountains, and into the heart of the shadowy abyss.

Chapter 6: The Battle of Shadows

The fellowship faced formidable challenges as they approached the epicenter of the dark forces. Malevolent creatures and sinister sorcery tested their unity and strength. Sarah's leadership and the bonds forged during their journey proved to be the key to overcoming these adversities.

The Battle of Shadows erupted in a spectacle of magic and might. As Sarah and her companions fought valiantly, the enchanted realm trembled under the weight of the impending confrontation between light and darkness.

Chapter 7: Triumph of Light

In the climactic showdown, Sarah confronted the source of the dark prophecy—an ancient entity fueled by hatred and despair. Drawing upon the power of the artifact and the unity of the fellowship, she unleashed a surge of pure light that shattered the darkness.

The realm basked in the radiance of restored balance. The once-threatened lands flourished with renewed life, and the fellowship celebrated their victory. Sarah, now a heroine of legend, returned to Ravenwood, forever changed by her extraordinary journey.

Epilogue: A New Beginning

Ravenwood embraced a new era of harmony, and Sarah became a guardian of the enchanted realm. The fellowship, bound by shared memories and a sense of profound purpose, continued to explore the magical lands beyond.

As for the dusty old book in Sarah's grandmother's attic, it found its place on a sacred shelf among other mystical tomes. Its pages whispered the tale of a brave girl who bridged worlds, and its magic lived on in the hearts of those who believed in the extraordinary.

And so, the story concluded, leaving the enchanted realm forever grateful for the courage of a girl named Sarah who dared to dream beyond the ordinary.

THE END

            """"


    val charLimit = 2500
    val chapters = paginateText(text, charLimit)

    var currentPage by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(bottom = 54.dp),
        scaffoldState = rememberScaffoldState(),
    ) {

        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = chapters[currentPage]
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currentPage > 0) {
                                currentPage--
                            }
                        },
                        enabled = currentPage > 0
                    ) {
                        Text("Previous Page")
                    }

                    Text(
                        fontSize = 22.sp,
                        text = "${currentPage + 1}/${chapters.size}"
                    )

                    Button(
                        onClick = {
                            if (currentPage < chapters.size - 1) {
                                currentPage++
                            }
                        },
                        enabled = currentPage < chapters.size - 1
                    ) {
                        Text("Next Page")
                    }
                }

                LaunchedEffect(scrollState, currentPage) {
                    scrollState.animateScrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun paginateText(text: String, charLimit: Int): List<String> {
    val chapters = mutableListOf<String>()
    var currentChapter = StringBuilder()
    var currentChapterCharCount = 0

    for (line in text.lines()) {
        if (line.startsWith("Chapter")) {
            // Nowy rozdział - dodaj poprzedni i zacznij nowy
            if (currentChapter.isNotBlank()) {
                chapters.add(currentChapter.toString().trim())
                currentChapter = StringBuilder(line)
                currentChapterCharCount = line.length
            } else {
                // Pierwszy rozdział
                currentChapter.append(line)
                currentChapterCharCount = line.length
            }
        } else {
            if (currentChapterCharCount + line.length + 1 <= charLimit) {
                currentChapter.append(line).append("\n")
                currentChapterCharCount += line.length + 1
            } else {
                // Nowa strona dla rozdziału
                chapters.add(currentChapter.toString().trim())
                currentChapter = StringBuilder(line)
                currentChapterCharCount = line.length
            }
        }
    }

    // Dodaj ostatni rozdział
    if (currentChapter.isNotBlank()) {
        chapters.add(currentChapter.toString().trim())
    }

    return chapters
}

//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun ReadScreen() {
//    var selectedText by remember { mutableStateOf<String?>(null) }
//    var selectedOffset by remember { mutableStateOf<Int?>(null) }
//    var clickPosition by remember { mutableStateOf(Offset(0f, 0f)) }
//
//    val text = """
//    Nie jesteś Słońcem, wokół którego chciałabym krążyć. Jesteś pieprzoną planetą
//    w najdalszym zakątku tego wszechświata, a moje życie jest poświęcone misji,
//    aby ją zbadać.
//
//    Kiedy patrzę w górę na nocne niebo, widzę Twoją obecność wśród gwiazd.
//    Jesteś jedną z nich, błyszczącą i oddaloną. Czasami wydaje się, że jesteś
//    bardzo daleko, ale moje serce zawsze jest z Tobą.
//
//    Twoja orbita przecina moje myśli, a Twoja grawitacja przyciąga moje marzenia.
//    Chociaż jesteśmy oddzieleni przez ogromną przestrzeń, nasza więź jest silniejsza
//    niż każda siła grawitacyjna.
//
//    Każde spojrzenie w Twoją stronę to podróż przez kosmiczne zakamarki uczuć.
//    Czasem czuję się jak podróżnik między galaktykami, szukający odpowiedzi na pytania
//    o naszą egzystencję.
//
//    Tak, nie jesteś Słońcem, ale dla mnie jesteś centrum tego wszechświata.
//    Twoja obecność nadaje sens mojemu życiu, a Twoje światło rozświetla moją duszę.
//
//    Jesteśmy jak dwie gwiazdy na niebie, które mimo odległości łączą swoje światła.
//    Niech nasza historia trwa wiecznie, choćbyśmy byli oddzieleni przez miliardy lat świetlnych.
//"""
//
//    val annotatedString = buildAnnotatedString {
//        withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
//            append(text)
//        }
//    }
//
//    ClickableText(
//        text = annotatedString,
//        modifier = Modifier
//            .padding(horizontal = 14.dp, vertical = 14.dp)
//            .pointerInteropFilter { event ->
//                if (event.action == MotionEvent.ACTION_DOWN) {
//                    clickPosition = if (event.x >= 930) {
//                        Offset(event.x - 100, event.y)
//                    } else if (event.x <= 150) {
//                        Offset(event.x, event.y)
//                    } else {
//                        Offset(event.x - 50, event.y)
//                    }
//                }
//                false
//            },
//        style = TextStyle(
//            color = Color.White,
//            textDecoration = TextDecoration.None
//        ),
//        onClick = { offset ->
//            val wordWithIndices = findWordAtIndex(text, offset)
//            selectedText = wordWithIndices.word
//            selectedOffset = offset
//        }
//    )
//
//    // Display the custom info box if selectedText is not null
//    if (!selectedText.isNullOrBlank() && selectedOffset != null) {
//        CustomInfoBox(
//            selectedText = selectedText?.lowercase() ?: "",
//            clickPosition = clickPosition,
//            onDismiss = {
//                selectedText = null
//                selectedOffset = null
//            }
//        )
//    }
//}
//
//@Composable
//fun CustomInfoBox(selectedText: String, clickPosition: Offset, onDismiss: () -> Unit) {
//    val context = LocalContext.current
//
//    val position = with(LocalDensity.current) {
//        Offset(
//            x = clickPosition.x / density,
//            y = (clickPosition.y / density) + 32
//        )
//    }
//
//    Box(
//        modifier = Modifier
//            .offset(position.x.dp, position.y.dp)
//            .background(Color.Gray, RoundedCornerShape(10.dp))
//            .padding(vertical = 6.dp, horizontal = 8.dp)
//            .clip(MaterialTheme.shapes.medium)
//            .clickable {
//                // You can add custom actions when the info box is clicked
//                onDismiss.invoke()
//            }
//    ) {
//
//        Row {
//            // Your content inside the info box goes here
//            Text(text = selectedText, color = Color.White)
//
//            Spacer(modifier = Modifier.width(4.dp))
//
//            Image(
//                modifier = Modifier
//                    .size(22.dp)
//                    .clickable {
//                        onDismiss.invoke()
//                        Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
//                    },
//                painter = painterResource(id = R.drawable.baseline_add_circle_box),
//                contentDescription = "add"
//            )
//        }
//    }
//}
//
//fun findWordAtIndex(text: String, offset: Int): WordWithIndices {
//    var startIndex = offset
//    var endIndex = offset
//
//    // Find the start of the word
//    while (startIndex > 0 && text[startIndex - 1] != ' ') {
//        startIndex--
//    }
//
//    // Find the end of the word
//    while (endIndex < text.length && text[endIndex] != ' ') {
//        endIndex++
//    }
//
//    // Get the word
//    val word = text.substring(startIndex, endIndex)
//
//    // Filter out punctuation
//    val filteredWord = word.filter { it.isLetterOrDigit() }
//
//    return WordWithIndices(filteredWord, startIndex, endIndex)
//}