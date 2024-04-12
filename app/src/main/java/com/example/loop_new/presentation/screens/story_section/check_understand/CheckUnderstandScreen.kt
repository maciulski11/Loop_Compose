package com.example.loop_new.presentation.screens.story_section.check_understand

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loop_new.R
import com.example.loop_new.presentation.navigation.NavigationSupport
import kotlinx.coroutines.launch
import java.util.Locale

data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
)

private val questions = listOf(
    Question(
        text = "What is the capital of France?",
        answers = listOf("London", "Paris", "Berlin"),
        correctAnswerIndex = 1
    ),
    Question(
        text = "What is the largest planet in our solar system?",
        answers = listOf("Jupiter", "Earth", "Mars"),
        correctAnswerIndex = 0
    ),
    Question(
        text = "Which is the largest ocean on Earth?",
        answers = listOf("Atlantic Ocean", "Indian Ocean", "Pacific Ocean"),
        correctAnswerIndex = 2
    ),
    Question(
        text = "What is the chemical symbol for water?",
        answers = listOf("H2O", "CO2", "O2"),
        correctAnswerIndex = 0
    ),
    Question(
        text = "Who is the author of 'Romeo and Juliet'?",
        answers = listOf("William Shakespeare", "Charles Dickens", "Jane Austen"),
        correctAnswerIndex = 0
    ),
    Question(
        text = "What is the boiling point of water in Celsius?",
        answers = listOf("100°C", "0°C", "-100°C"),
        correctAnswerIndex = 0
    )
)

@Composable
fun CheckUnderstandScreen(navController: NavController) {
    var submitClicked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val selectedAnswers =
        remember { mutableStateListOf(*Array<Int?>(questions.size) { null }) }
    val colors = remember { mutableStateListOf(*Array<Color?>(questions.size) { null }) }

    //Calculating the number of correct answers
    val correctCount = remember { mutableIntStateOf(0) }
    val totalQuestions = questions.size
    val correctAnswers = correctCount.intValue
    val percentage = ((correctAnswers.toDouble() / totalQuestions.toDouble()) * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        questions.forEachIndexed { index, question ->
            QuestionCard(
                question = question,
                selectedAnswerIndex = selectedAnswers[index],
                color = colors[index],
                onAnswerSelected = { answerIndex ->
                    if (!submitClicked) { // Umożliwia zmianę odpowiedzi przed zatwierdzeniem
                        selectedAnswers[index] = answerIndex
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        AnimatedVisibility(!allAnswersSelected(selectedAnswers) && !submitClicked) {
            Text(
                text = "Zaznacz wszystkie odpowiedzi przed zatwierdzeniem.",
                style = MaterialTheme.typography.body1,
                color = Color.Red
            )
        }

        if (!submitClicked) {
            IconButton(
                onClick = {
                    if (allAnswersSelected(selectedAnswers)) {
                        submitClicked = true
                        selectedAnswers.forEachIndexed { index, answerIndex ->
                            val question = questions[index]

                            colors[index] = if (answerIndex == question.correctAnswerIndex) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        correctCount.intValue = 0
                        for (index in questions.indices) {
                            val question = questions[index]
                            val answerIndex = selectedAnswers[index]
                            if (answerIndex != null && answerIndex == question.correctAnswerIndex) {
                                correctCount.intValue++
                            }
                        }
                    }
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.check3d), // Zastąp id zasobu obrazka odpowiednim zasobem
                    contentDescription = null, // Zastąp opisem, jeśli to konieczne
                    modifier = Modifier
                        .size(70.dp)
                        .padding(
                            top = 4.dp
                        )
                )
            }
        }

        //View your score after completing the quiz
        AnimatedVisibility(
            visible = submitClicked,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {

            //Scroll to the end when the result appears
            SideEffect {
                if (submitClicked) {
                    scope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                ) {
                    Text(
                        text = "Uzyskałeś ${correctCount.value}/${questions.size} poprawnych odpowiedzi.",
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Zrozumiałeś historię w $percentage%.",
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        color = Color.Black,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .align(Alignment.Center)
                    ) {
                        Button(
                            onClick = {
                                navController.navigate(NavigationSupport.StoryScreen)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                        ) {
                            Text(
                                text = "Exit".toUpperCase(Locale.ROOT),
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

//Function to check if all answers have been selected
private fun allAnswersSelected(selectedAnswers: List<Int?>): Boolean {
    return selectedAnswers.none { it == null }
}

@Composable
private fun QuestionCard(
    question: Question,
    selectedAnswerIndex: Int?,
    color: Color?,
    onAnswerSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = question.text, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        question.answers.forEachIndexed { index, answer ->
            AnswerOption(
                text = answer,
                isSelected = index == selectedAnswerIndex,
                color = color,
                onClick = { onAnswerSelected(index) },
            )
        }
    }
}

@Composable
private fun AnswerOption(
    text: String,
    isSelected: Boolean,
    color: Color?,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) {
        color ?: Color.Yellow
    } else {
        Color.LightGray
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(enabled = true, onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.Black else Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}