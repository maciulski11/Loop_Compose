package com.example.loop_new.domain.model.firebase

data class Flashcard (
    val word: String? = null,
    val translate: String? = null,
    val meaning: String? = null,
    val example: String? = null,
    val partOfSpeech: String? = null,
    val pronunciation: String? = null,
    val audioUrl: String? = null,
    val uid: String? = null,
    var isFrontVisible: Boolean? = null,
    var lastStudiedDate: Long? = null,
    var nextStudyDate: Long? = null,
)