package com.example.loop_new.domain.model.firebase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repeat_section")
data class RepeatSection(
    val word: String? = null,
    val translate: String? = null,
    val meaning: String? = null,
    val example: String? = null,
    val partOfSpeech: String? = null,
    val pronunciation: String? = null,
    val audioUrl: String? = null,
    val boxId: Int? = null,
    @PrimaryKey
    val flashcardId: Int? = null
)