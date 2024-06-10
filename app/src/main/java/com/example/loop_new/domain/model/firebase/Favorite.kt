package com.example.loop_new.domain.model.firebase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "story", indices = [Index(value = ["uid"], unique = true)])
data class Favorite(
    val title: String? = null,
    val author: String? = null,
    val entry: String? = null,
    val uid: String? = null,
    val level: String? = null,
    val category: String? = null,
    val image: String? = null,
    val favorite: Boolean? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)

@Entity(tableName = "text_content",
    foreignKeys = [
        ForeignKey(Favorite::class,
            parentColumns = ["id"],
            childColumns = ["storyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chapter", "storyId"], unique = true)]
)

data class TextContentRoom(
    val chapter: String? = null,
    val content: String? = null,
    val indexOfChapter: Int? = null,
    val storyUid: String? = null,
    val storyId: Int? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)
