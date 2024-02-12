package com.example.loop_new.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.loop_new.domain.model.firebase.Story


data class Category(val name: String, val books: List<String>)

@Composable
fun TestScreen(categories: List<Category>) {
// W zakresie testowym możesz zdefiniować stałe dane
    val categories = listOf(
        Category(
            name = "Action",
            books = listOf("Book 1", "Book 2", "Book 3")
        ),
        Category(
            name = "Drama",
            books = listOf("Book A", "Book B", "Book C")
        ),
        Category(
            name = "Science Fiction",
            books = listOf("Book X", "Book Y", "Book Z")
        ),
        // Dodaj więcej kategorii według potrzeb
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(categories) { category ->
            this@LazyColumn.item {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            this@LazyColumn.item {
                LazyRow(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(category.books) { book ->
                        BookItem(book = book)
                    }
                }
            }
        }
    }
}

@Composable
fun BookItem(book: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            Text(
                text = book,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LazyColumnWithLazyRowsPreview() {
    val categories = List(6) { index ->
        Category(
            name = "Category $index",
            books = List(10) { "Book $index-$it" }
        )
    }

    TestScreen(categories)
}






