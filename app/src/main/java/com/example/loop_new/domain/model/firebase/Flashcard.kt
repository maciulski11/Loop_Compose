package com.example.loop_new.domain.model.firebase

import com.google.firebase.Timestamp

data class Flashcard(
    val word: String? = null,
    val translate: String? = null,
    val meaning: String? = null,
    val example: String? = null,
    val partOfSpeech: String? = null,
    val pronunciation: String? = null,
    val audioUrl: String? = null,
    val uid: String? = null,
    val knowledgeLevel: String? = KnowledgeLevel.NEW_WORD.value,
    var lastStudiedDate: Timestamp? = null,
    var nextStudyDate: Timestamp? = null,
    var isReadyToStudy: Boolean? = null
)

enum class KnowledgeLevel(val value: String) {
    NEW_WORD("newWord"),
    KNOW("know"),
    SOMEWHAT_KNOW("somewhatKnow"),
    DO_NOT_KNOW("doNotKnow")
}