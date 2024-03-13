package com.example.loop_new.domain.model.firebase

data class Box(
    val name: String? = null,
    val describe: String? = null,
    val uid: String? = null,
    val color1: String? = null,
    val color2: String? = null,
    val color3: String? = null,
    val permissionToEdit: Boolean? = null,
    val addFlashcardFromStory: Boolean? = null
)