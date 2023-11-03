package com.example.loop_new.domain.model

data class Box(
    val name: String? = null,
    val describe: String? = null,
    val uid: String? = null,
    val flashcards: List<Flashcard>? = null
)