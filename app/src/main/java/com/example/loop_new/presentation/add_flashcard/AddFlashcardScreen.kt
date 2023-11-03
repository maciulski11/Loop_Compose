package com.example.loop_new.presentation.add_flashcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.loop_new.R
import com.example.loop_new.ui.theme.Typography

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddFlashcardScreenPreview() {
    AddFlashcardScreen()
}

@Composable
fun AddFlashcardScreen() {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (returnButton, title, wordLinearLayout, translateLinearLayout, partOfSpeechLinearLayout, meanLinearLayout, sentenceLinearLayout, addButton) = createRefs()

        val wordEditText = remember { mutableStateOf(TextFieldValue()) }
        val translateEditText = remember { mutableStateOf(TextFieldValue()) }
        val partOfSpeechEditText = remember { mutableStateOf(TextFieldValue()) }
        val meanEditText = remember { mutableStateOf(TextFieldValue()) }
        val sentenceEditText = remember { mutableStateOf(TextFieldValue()) }

        // Return Button
//        Image(
//            painter = painterResource(id = R.drawable.baseline_add_circle_box),
//            contentDescription = null, // change this
//            modifier = Modifier
//                .constrainAs(returnButton) {
//                    top.linkTo(parent.top, margin = 8.dp)
//                    start.linkTo(parent.start, margin = 8.dp)
//                }
//                .padding(4.dp),
//
//        )

        // Title Text
        Text(
            text = "Creating flashcard:",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 6.dp)
                    start.linkTo(returnButton.end)
                },
            textAlign = TextAlign.Center,

            )

        // Word EditText
        BasicTextField(
            value = wordEditText.value,
            onValueChange = { wordEditText.value = it },
            modifier = Modifier
                .border(2.dp, Color.Black, shape = RoundedCornerShape(12.dp)) // Obramowanie
                .constrainAs(wordLinearLayout) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                }
//                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(Color.Gray)
                .padding(
                    top = 6.dp,
                    start = 112.dp,
                    end = 112.dp
                ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Handle Next button click
                    // Move focus to the next field
                }
            ),
            textStyle = TextStyle(
                color = Color.Black, // Kolor tekstu
                fontSize = 20.sp, // Rozmiar tekstu
                fontWeight = FontWeight.Bold // Styl tekstu
            ),
            singleLine = true, // Pozwala na jednoliniowe wpisywanie tekstu
            cursorBrush = SolidColor(Color.Black), // Kolor kursora
        )

        // ... Repeat the above code for other fields (translate, partOfSpeech, mean, sentence)

        // Add Button
        FloatingActionButton(
            onClick = {
                // Handle add button click
                // Get the values from the text fields and add the flashcard
            },
            modifier = Modifier
                .constrainAs(addButton) {
                    bottom.linkTo(parent.bottom, margin = 52.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_check),
                contentDescription = null, // change this
            )
        }
    }
}
