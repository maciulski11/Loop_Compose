package com.example.loop_new.domain.model.firebase


data class User(
    val email: String? = null,
    val uid: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val favoriteStories: List<FavoriteStory>? = null
)

data class FavoriteStory(
    val uid: String? = null,
    val userUid: String? = null,
    val category: String? = null,
    val favorite: Boolean? = null
)