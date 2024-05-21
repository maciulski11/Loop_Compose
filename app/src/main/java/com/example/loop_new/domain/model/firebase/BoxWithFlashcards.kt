package com.example.loop_new.domain.model.firebase

import androidx.room.Embedded
import androidx.room.Relation

data class BoxWithFlashcards(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "id",
        entityColumn = "boxId"
    )
    val flashcards: List<Flashcard>
)