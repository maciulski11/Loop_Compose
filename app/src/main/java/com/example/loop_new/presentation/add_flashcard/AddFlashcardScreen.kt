package com.example.loop_new.presentation.add_flashcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (
            title,
            wordLinearLayout,
            translateLinearLayout,
            partOfSpeechLinearLayout,
            meanLinearLayout,
            exampleLinearLayout,
            addButton
        ) = createRefs()

        val wordEditText = remember { mutableStateOf(TextFieldValue()) }
        val translateEditText = remember { mutableStateOf(TextFieldValue()) }
        val partOfSpeechEditText = remember { mutableStateOf(TextFieldValue()) }
        val meanEditText = remember { mutableStateOf(TextFieldValue()) }
        val exampleEditText = remember { mutableStateOf(TextFieldValue()) }
        var translateCheckBox by remember { mutableStateOf(true) }
        var partOfSpeechCheckBox by remember { mutableStateOf(true) }
        var meanCheckBox by remember { mutableStateOf(true) }
        var exampleCheckBox by remember { mutableStateOf(true) }

        // Title Text
        Text(
            text = "Creating flashcard:",
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 14.dp)
                },
            textAlign = TextAlign.Center,
        )

        // Word EditText
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .fillMaxWidth()
                .constrainAs(wordLinearLayout) {
                    top.linkTo(title.bottom, margin = 14.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)), // Obramowanie
            verticalAlignment = Alignment.CenterVertically

        ) {

            OutlinedTextField(
                value = wordEditText.value,
                onValueChange = { wordEditText.value = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                ),
                singleLine = false,
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "word",
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, // Kolor obramowania w trybie skupienia
                    unfocusedBorderColor = Color.Transparent, // Kolor obramowania w trybie bez skupienia
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Image(
                painter = painterResource(id = R.drawable.dictionary),
                contentDescription = "Button",
                modifier = Modifier
                    .size(62.dp)
                    .align(Alignment.CenterVertically)
                    .padding(end = 2.dp)
                    .clickable { }
            )
        }

        // Translate EditText
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .fillMaxWidth()
                .constrainAs(translateLinearLayout) {
                    top.linkTo(wordLinearLayout.bottom, margin = 6.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)) // Obramowanie

        ) {

            Checkbox(
                checked = translateCheckBox,
                onCheckedChange = { translateCheckBox = it },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)

            )

            OutlinedTextField(
                value = translateEditText.value,
                onValueChange = { translateEditText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                ),
                singleLine = false,
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "translate",
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, // Kolor obramowania w trybie skupienia
                    unfocusedBorderColor = Color.Transparent, // Kolor obramowania w trybie bez skupienia
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
        }

        // PartOfSpeech EditText
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .fillMaxWidth()
                .constrainAs(partOfSpeechLinearLayout) {
                    top.linkTo(translateLinearLayout.bottom, margin = 6.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)) // Obramowanie

        ) {

            Checkbox(
                checked = partOfSpeechCheckBox,
                onCheckedChange = { partOfSpeechCheckBox = it },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)

            )

            OutlinedTextField(
                value = partOfSpeechEditText.value,
                onValueChange = { partOfSpeechEditText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                ),
                singleLine = false,
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "part of speech",
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, // Kolor obramowania w trybie skupienia
                    unfocusedBorderColor = Color.Transparent, // Kolor obramowania w trybie bez skupienia
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
        }

        // Mean EditText
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .fillMaxWidth()
                .constrainAs(meanLinearLayout) {
                    top.linkTo(partOfSpeechLinearLayout.bottom, margin = 6.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)) // Obramowanie

        ) {

            Checkbox(
                checked = meanCheckBox,
                onCheckedChange = { meanCheckBox = it },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)

            )

            OutlinedTextField(
                value = meanEditText.value,
                onValueChange = { meanEditText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                ),
                singleLine = false,
                maxLines = 5,
                placeholder = {
                    Text(
                        text = "mean",
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, // Kolor obramowania w trybie skupienia
                    unfocusedBorderColor = Color.Transparent, // Kolor obramowania w trybie bez skupienia
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
        }


        // Example EditText
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .fillMaxWidth()
                .constrainAs(exampleLinearLayout) {
                    top.linkTo(meanLinearLayout.bottom, margin = 6.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)) // Obramowanie

        ) {

            Checkbox(
                checked = exampleCheckBox,
                onCheckedChange = { exampleCheckBox = it },
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)

            )

            OutlinedTextField(
                value = exampleEditText.value,
                onValueChange = { exampleEditText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                ),
                singleLine = false,
                maxLines = 5,
                placeholder = {
                    Text(
                        text = "example",
                        fontSize = 24.sp,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent, // Kolor obramowania w trybie skupienia
                    unfocusedBorderColor = Color.Transparent, // Kolor obramowania w trybie bez skupienia
                    textColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
        }

        // Add Button
        FloatingActionButton(
            onClick = {
                // Handle add button click
                // Get the values from the text fields and add the flashcard
            },
            modifier = Modifier
                .constrainAs(addButton) {
                    bottom.linkTo(parent.bottom, margin = 36.dp)
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
