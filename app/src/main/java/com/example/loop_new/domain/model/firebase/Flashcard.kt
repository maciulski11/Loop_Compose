package com.example.loop_new.domain.model.firebase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard",
    foreignKeys = [
        ForeignKey(entity = Box::class,
            parentColumns = ["id"],
            childColumns = ["boxId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["boxId"])] // Dodanie indeksu dla kolumny boxId
)

data class Flashcard(
    val word: String? = null,
    val translate: String? = null,
    val meaning: String? = null,
    val example: String? = null,
    val partOfSpeech: String? = null,
    val pronunciation: String? = null,
    val audioUrl: String? = null,
    val boxId: Int? = null,
    val uid: String? = null,
    val boxUid: String? = null,
    val knowledgeLevel: String? = KnowledgeLevel.NEW_WORD.value,
//    var lastStudiedDate: Timestamp? = null,
//    var nextStudyDate: Timestamp? = null,
    var isReadyToStudy: Boolean? = null,
    @PrimaryKey(autoGenerate = true)
    val idFlashcard: Int = 0
)

enum class KnowledgeLevel(val value: String) {
    NEW_WORD("newWord"),
    KNOW("know"),
    SOMEWHAT_KNOW("somewhatKnow"),
    DO_NOT_KNOW("doNotKnow")
}