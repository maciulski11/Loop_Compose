package com.example.loop_new.presentation.screens.read

import android.annotation.SuppressLint
import android.text.Html
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Button
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
import com.example.loop_new.R
import com.example.loop_new.presentation.screens.story.StoryViewModel
import com.example.loop_new.ui.theme.White
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class WordWithIndices(val word: String, val start: Int, val end: Int)

@Preview(showBackground = true)
@Composable
fun StoryScreenPreview() {
//    ReadScreen()
}

val text = """

    # Entry:

    In the tranquil town of Crestwood, where the whispers of secrets lingered in the narrow alleys, Detective Alex Turner found himself caught in a web of deception. It all commenced on a foggy evening when the moon barely pierced through the thick mist, casting long shadows over the cobblestone streets. 

    A well-known local businessman, Richard Hawthorne, was discovered lifeless in his lavish mansion. The initial assumption was a heart attack, but as Detective Turner delved deeper, ominous clues began to emerge. The deceased's financial records revealed a complex network of shady transactions, raising suspicions of foul play.

    As Turner navigates the labyrinth of Hawthorne's dark dealings, he uncovers long-concealed secrets that link the victim to a notorious underworld figure. The detective must now unravel the threads of deceit, facing challenges that jeopardize not only his investigation but also the safety of those closest to him.

    Little does Turner know that the shadows concealing the truth are deeper than he could have imagined. In this intricate dance of deception, the line between ally and adversary blurs, and every step taken leads him closer to a truth that could shatter the fragile tranquility of Crestwood forever.

    Will Detective Alex Turner unravel the enigma in time, or will the shadows of deceit cast their ominous veil over Crestwood, burying its secrets in eternal darkness?

    # Chapter 1: Unveiling Shadows

    Detective Alex Turner paced anxiously in his cluttered office, the dim glow of a single desk lamp casting elongated shadows on the worn-out floor. The rhythmic tap of rain against the windowpane echoed the unease that enveloped Crestwood.

    The day had begun like any other until the call came – Richard Hawthorne, a man of influence and wealth, found dead in his sprawling mansion. Turner's instincts kicked in, recognizing the underlying currents of something more sinister than a mere natural demise.

    As he approached the Hawthorne estate, the rain intensified, adding a somber note to the scene. The imposing mansion loomed through the mist, its windows reflecting the ghostly pallor of the investigator's face. Turner's mind buzzed with questions, each step towards the entrance echoing the weight of the impending revelation.

    Inside, the air was thick with an unsettling stillness. The deceased lay in repose, his lifeless eyes seemingly holding secrets even in death. Turner scrutinized the room, the opulence of Hawthorne's life contrasting sharply with the hidden shadows that lurked beneath the surface.

    The initial examination suggested a heart attack, but Turner's intuition urged him to dig deeper. The businessman's financial records hinted at transactions that danced on the fringes of legality. As Turner navigated the labyrinth of numbers and accounts, a clandestine world emerged – one woven with betrayal and clandestine alliances.

    The detective's investigation drew attention to Hawthorne's connections with a mysterious figure known only as "The Whisperer." Rumored to be the puppet master orchestrating the town's clandestine affairs, The Whisperer's influence cast a long shadow over Crestwood.

    As Turner delved further into the shadows, he encountered the enigmatic Selena Drake, a woman with a cryptic past entwined with the deceased. Her piercing gaze held secrets yet to be unveiled, adding another layer of complexity to the investigation.

    The town's underbelly began to stir, reacting to the detective's relentless pursuit of truth. Turner found himself entangled in a dangerous game of cat and mouse, where allegiances shifted like shadows in the dark.

    With each revelation, Crestwood's façade crumbled, exposing a world of deceit that threatened to consume all who dared to uncover its secrets. As Detective Alex Turner navigates the treacherous path ahead, he realizes that in the shadows lie not only answers but also the lurking danger that could swallow him whole.

    In this intricately woven tale of suspense, Detective Turner takes his first steps into a labyrinth of deception, where every discovery unravels a new layer of mystery, plunging him deeper into the Shadows of Deceit.

    # Chapter 2: Echoes of Betrayal

    Detective Alex Turner's restless nights blended into days consumed by the relentless pursuit of truth. Crestwood, once a tranquil haven, transformed into a labyrinth of secrets that threatened to ensnare him. As he sifted through the remnants of Richard Hawthorne's life, the town's shadows whispered tales of betrayal.

    Turner's investigation led him to Hawthorne's estranged business partner, Douglas Blackwood, a man with a facade as impenetrable as the mansion's stone walls. Blackwood's alibis crumbled under scrutiny, revealing a web of deception carefully woven around the deceased businessman.

    Amidst the labyrinth of lies, Turner found solace in the company of Selena Drake. Her enigmatic presence mirrored the shadows he sought to unveil. Together, they navigated the intricate dance of secrets and half-truths, each step bringing them closer to the elusive Whisperer.

    The detective's pursuit intensified as he unearthed a clandestine meeting ground – a hidden club known only to those versed in the clandestine affairs of Crestwood. The air within the dimly lit establishment crackled with tension, and the clandestine murmurs hinted at a conspiracy that transcended the boundaries of the town.

    As Turner delved into the club's underbelly, he encountered a network of individuals bound by a pact of silence. The walls echoed with the whispers of those who dared not speak aloud. Every clandestine meeting, every exchanged glance, painted a portrait of a town entangled in a web of power and betrayal.

    Blackwood's connections led Turner to the threshold of the Whisperer's domain. The detective felt the weight of the town's secrets pressing down on him as he approached the elusive figure's lair. The Whisperer's identity remained a mirage, slipping through Turner's grasp like elusive shadows.

    In the dim glow of his office, Turner pieced together the fragments of the investigation. The threads of deceit intertwined with the town's history, creating a tapestry of lies that threatened to unravel the very fabric of Crestwood.

    As Turner prepared to confront the Whisperer, he sensed the shadows closing in. The lines between ally and adversary blurred, and the detective found himself entangled in a conspiracy that reached far beyond the confines of Crestwood.

    In this unfolding saga of Shadows of Deceit, Detective Alex Turner faced a choice – to retreat into the safety of the known or to confront the shadows that danced on the edge of revelation. Little did he know that the path ahead held more twists and turns, and the echoes of betrayal would resonate through the darkened corridors of Crestwood.

    # Chapter 3: Veil of Shadows

    In the heart of Crestwood, Detective Alex Turner stood at the crossroads of truth and treachery. The town's murky history cast a veil over his pursuit, leaving him entangled in a labyrinth of deceit. The revelations of the past echoed through the present, each step forward unraveling a tapestry woven with the threads of betrayal.

    As Turner delved deeper into the town's archives, he uncovered a clandestine operation that connected Hawthorne and Blackwood to a series of unsolved crimes. The shadows of the past reached out, ensnaring the detective in a sinister dance where every revelation hinted at a darker truth.

    Selena Drake, once a confidante, now held secrets that could shatter the fragile trust between them. The detective's journey took an unexpected turn as he discovered Selena's connection to the clandestine club, where whispers of conspiracy mingled with the smoke-filled air.

    The Whisperer's lair, hidden in the recesses of Crestwood, became the focal point of Turner's investigation. The detective navigated the treacherous terrain, where allies wore masks, and enemies lurked in the shadows. The veil of secrecy lifted, revealing a nexus of power and corruption that extended beyond Crestwood's borders.

    Turner's pursuit of the elusive figure reached its zenith as he confronted the Whisperer. The dimly lit room bore witness to a tense exchange, where words held the weight of unspoken truths. The Whisperer's identity remained shrouded, a phantom orchestrating the town's descent into darkness.

    In this penultimate chapter of Shadows of Deceit, Detective Alex Turner stood on the precipice, torn between the desire for justice and the realization that some shadows refused to dissipate. As the veil tightened, Turner prepared for the final act, where the boundaries between right and wrong blurred, and Crestwood's destiny hung in the balance.

    # Chapter 4: Echoes of Redemption

    As the first light of dawn painted the horizon, Crestwood stood on the precipice of redemption. Detective Alex Turner's pursuit of justice had become an odyssey, and the town's fate now hinged on the decisions made in the crucible of shadows.

    The revelation of the Whisperer's identity sent shockwaves through Crestwood, exposing a figure entwined in the town's history like a malevolent specter. In a clandestine meeting at the heart of the Whisperer's lair, the detective unmasked Selena Drake as the orchestrator of the shadows. Her motives, a mosaic of vengeance and retribution, came to light as the detective delved into her tormented past.

    The final confrontation unfolded against the backdrop of Crestwood's decaying beauty. The town square, once witness to celebrations, now bore the scars of a clandestine war. Turner and Selena faced off, their destinies intertwined in a delicate dance between truth and absolution.

    As the detective confronted Selena, the echoes of redemption reverberated through Crestwood's cobblestone streets. The choices made in those fleeting moments held the power to reshape the town's narrative. Turner grappled with the complexities of justice, weighing the thin line between punishment and forgiveness.

    In the climax of Shadows of Deceit, Crestwood became a crucible where secrets met reckoning, and the shadows released their grip on the town's soul. The detective's journey reached its zenith as he navigated the moral labyrinth, confronting the consequences of his actions and the ghosts that haunted Selena.

    The denouement unfolded against the canvas of a town yearning for renewal. As Turner made his final decisions, Crestwood emerged from the shadows, bathed in the soft glow of a sunrise that promised a new beginning. The resolute steps toward redemption echoed through the town, leaving behind the remnants of a clandestine past.

    In the final pages of Shadows of Deceit, Detective Alex Turner stood as a sentinel of justice, and Crestwood embraced the dawn of its redemption. The tale concluded, leaving the town forever changed by the echoes of a journey that transcended shadows and illuminated the path to renewal.



    THE END

    """.trimIndent()


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReadScreen(viewModel: ReadViewModel) {
    var selectedText by remember { mutableStateOf<String?>(null) }
    var selectedOffset by remember { mutableStateOf<Int?>(null) }
    var clickPosition by remember { mutableStateOf(Offset(0f, 0f)) }

    val charLimit = 2000

    val chapters = paginateText(viewModel.story?.text.toString(), charLimit)

    var currentPage by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    fun createAnnotatedStringForLine(line: String): AnnotatedString {
        return buildAnnotatedString {
            val parts = line.split("@") // Rozdziel tekst na części w miejscach wystąpienia @
            parts.forEachIndexed { index, part ->
                when {
                    part.startsWith("#") -> {
                        // Tytuł rozdziału
                        withStyle(
                            style = SpanStyle(
                                color = Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(part.replace("#", "").trim())
                            append("\n")
                        }
                    }

                    part.contains("#") -> {
                        // Jeśli fragment zawiera #, ale nie zaczyna się od niego
                        val splitPart = part.split("#")
                        splitPart.forEachIndexed { i, subPart ->
                            if (i % 2 == 0) {
                                // Nieparzyste indeksy oznaczają tekst bezpośrednio przed lub po #
                                withStyle(style = SpanStyle(color = Black, fontSize = 18.sp)) {
                                    append(subPart.trim())
//                                    append("\n")
                                }
                            } else {
                                // Parzyste indeksy oznaczają tekst między #
                                withStyle(
                                    style = SpanStyle(
                                        color = Black,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(subPart.trim())
//                                    append("\n")
                                }
                            }
                        }
                    }

                    part.isNotBlank() -> {
                        // Treść
                        withStyle(style = SpanStyle(color = Black, fontSize = 18.sp)) {
                            append(part.trim())
                        }
                    }
                }

//                if (index < parts.size - 1) {
//                    // Dodaj pusty wiersz między kolejnymi częściami
//                    append("\n")
//                }
            }
        }
    }

    // Tworzy całą AnnotatedString dla strony
    fun createAnnotatedStringForPage(page: Int): AnnotatedString {
        val lines = chapters[page].lines()
        return buildAnnotatedString {
            lines.forEach { line ->
                // Wykorzystuje wcześniej zdefiniowaną funkcję dla każdej linii
                append(createAnnotatedStringForLine(line))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 54.dp)
            .verticalScroll(state = scrollState)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            ClickableText(
                text = createAnnotatedStringForPage(currentPage),
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

                            selectedText = null
                            selectedOffset = null
                        }
                        false
                    },
                style = TextStyle(
                    color = Color.White,
                    textDecoration = TextDecoration.None
                ),
                onClick = { offset ->
                    val wordWithIndices = findWordAtIndex(chapters[currentPage], offset)
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

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

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
        }
        LaunchedEffect(scrollState, currentPage) {
            scrollState.animateScrollTo(0)
        }
    }
}

@Composable
fun paginateText(text: String, charLimit: Int): List<String> {
    val chapters = mutableListOf<String>()
    var currentChapter = StringBuilder()
    var currentChapterCharCount = 0

    for (line in text.lines()) {

        if (line.startsWith("#")) {
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
                        Toast
                            .makeText(context, "Successfully added!", Toast.LENGTH_SHORT)
                            .show()
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

    return WordWithIndices(filteredWord, startIndex, endIndex)
}


// Rozszerzenie do Char na sprawdzenie, czy jest znakiem interpunkcyjnym
fun Char.isPunctuation(): Boolean {
    return this in setOf(',', '.', ';', ':', '!', '?', '#')
}
