package com.example.loop_new.presentation.add_flashcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.loop_new.R
import com.example.loop_new.ui.theme.Typography

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddFlashcardScreenPreview() {
    val navController = rememberNavController()

//    Screen(navController = navController) { _, _, _ -> }
}

@Composable
fun AddFlashcardScreen(
    navController: NavController,
    viewModel: AddFlashcardViewModel,
    boxUid: String
) {
    Screen(
        viewModel,
        navController,
        { word, translate, pronunciation ->
            viewModel.addFlashcard(word, translate, pronunciation, boxUid)
        },
        { word ->
            viewModel.fetchInfoOfWord(word)
        }
    )
}

@Composable
fun Screen(
    viewModel: AddFlashcardViewModel,
    navController: NavController,
    onAddFlashcard: (word: String, translate: String, pronunciation: String) -> Unit,
    onFetchWord: (word: String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // EditText
            var wordEditText by remember { mutableStateOf("") }
            val translateEditText by remember { mutableStateOf("") }
            var partOfSpeechEditText by remember { mutableStateOf("") }
            var meanEditText by remember { mutableStateOf("") }
            var exampleEditText by remember { mutableStateOf("") }
            val pronunciationEditText by remember { mutableStateOf("") }

            // CheckBox
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
                    .padding(12.dp),
                textAlign = TextAlign.Center,
            )

            // Word EditText
            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically

            ) {

                OutlinedTextField(
                    value = wordEditText,
                    onValueChange = { wordEditText = it },
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
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        textColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                )

                Image(
                    painter = painterResource(id = R.drawable.dictionary),
                    contentDescription = "Button",
                    modifier = Modifier
                        .size(62.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 2.dp)
                        .clickable {
                            onFetchWord(wordEditText)
//                            TranslateService().testTranslateFunction()
                        }
                )
            }

            // Translate EditText
            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )

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
                    value = viewModel.translate,
                    onValueChange = { viewModel.translate = it },
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
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
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
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )

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
                    value = partOfSpeechEditText,
                    onValueChange = { partOfSpeechEditText = it },
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
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
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
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )

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
                    value = viewModel.meaning,
                    onValueChange = { viewModel.meaning = it },
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
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
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
                    .border(
                        2.dp,
                        Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
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
                    value = viewModel.example,
                    onValueChange = { viewModel.example = it },
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
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        textColor = Color.Black,
                        cursorColor = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Add Flashcard Button
            FloatingActionButton(
                onClick = {
                    onAddFlashcard(wordEditText, translateEditText, pronunciationEditText)
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 6.dp,
                        bottom = 26.dp
                    ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_check),
                    contentDescription = null,
                )
            }
        }
    }
}


