package com.example.loop_new.domain.model.firebase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class Story(
    val title: String? = null,
    val text: List<TextContent>? = null,
    val author: String? = null,
    val entry: String? = null,
    val uid: String? = null,
    val level: String? = null,
    val category: String? = null,
    val image: String? = null,
    val favoriteStories: List<FavoriteStory>? = null,
    val viewList: List<String>? = null,
    val favorite: Boolean? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    )

data class TextContent(
    val chapter: String? = null,
    val content: String? = null,
)
