package com.example.loop_new.domain.model.firebase

data class Story(
    val title: String? = null,
    val text: List<TextContent>? = null,
    val author: String? = null,
    val uid: String? = null,
    val level: String? = null,
    val category: String? = null,
    val image: String? = null,
    val favoriteStories: List<FavoriteStory>? = null,
    val viewList: List<String>? = null,
    val favorite: Boolean? = null
    )

data class TextContent(
    val chapter: String? = null,
    val content: String? = null,
)
