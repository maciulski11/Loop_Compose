package com.example.loop_new.domain.model.firebase

data class Story(
    val title: String? = null,
    val text: List<TextContent>? = null,
    val uid: String? = null,
    val image: String? = null
)

data class TextContent(
    val chapter: String? = null,
    val content: String? = null
)
