package com.example.loop_new.domain.model.firebase

import androidx.room.Embedded
import androidx.room.Relation

data class FavoriteStoryWithChapters (
    @Embedded val favorite: Favorite,
    @Relation(
        parentColumn = "id",
        entityColumn = "storyId"
    )
    val textContent: List<TextContentRoom>
)