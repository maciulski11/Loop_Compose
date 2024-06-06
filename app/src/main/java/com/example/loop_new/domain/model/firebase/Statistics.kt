package com.example.loop_new.domain.model.firebase

data class Statistics (
    val allFlashcards: List<AllFlashcards>,
){
    // Konstruktor bezargumentowy
    constructor() : this(emptyList())
}

data class AllFlashcards(
    val uid: String? = null,
    val status: String? = null
)

data class StatsSummary(
    val totalFlashcards: Int,
    val knowCount: Int,
    val somewhatKnow: Int,
    val doNotKnowCount: Int
)

data class LessonsData(
    val uidFlashcard: String? = null,
    val status: String? = null
)