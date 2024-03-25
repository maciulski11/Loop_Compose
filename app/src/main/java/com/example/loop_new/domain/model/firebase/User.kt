package com.example.loop_new.domain.model.firebase

import java.sql.Timestamp

data class User(
    val email: String? = null,
    val uid: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val favoriteStories: List<FavoriteStory>? = null,
    val stats: List<Stats>? = null
)

data class FavoriteStory(
    val uid: String? = null,
    val userUid: String? = null,
    val category: String? = null,
    val favorite: Boolean? = null,
)

data class Stats(
    val progressList: List<ProgressList>,
    val progressKnowledge: String? = null
)

data class ProgressList(
    val uidFlashcard: String? = null,
    val data: Timestamp? = null,
    val status: String? = null
)