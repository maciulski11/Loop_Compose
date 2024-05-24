package com.example.loop_new.domain.services

import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Category
import com.example.loop_new.domain.model.firebase.Statistics
import com.example.loop_new.domain.model.firebase.StatsSummary
import com.example.loop_new.domain.model.firebase.Story
import com.example.loop_new.domain.model.firebase.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface FirebaseService {

    fun createNewGoogleUser()

    fun getSignedInUser(): User?

    // Flashcards
    fun addPublicBoxToPrivateSection(boxUid: String)

    fun fetchListOfPublicBox(lastDocSnapshot: DocumentSnapshot?): Flow<Pair<List<Box>, DocumentSnapshot?>>

    fun fetchListOfFlashcardInPublicBox(boxUid: String): Flow<List<Flashcard>>

    // Story
    fun fetchListOfStory(): Flow<Category>

    suspend fun fetchAllStoriesFromOneCategory(category: String): List<Story>

    suspend fun fetchStory(storyUid: String): Story?

    suspend fun fetchFavoriteStories(): List<Story>

    suspend fun addStoryToFavoriteSection(storyId: String, category: String)

    suspend fun removeStoryFromFavoriteSection(storyId: String, category: String)

    fun addStoryUidToViewList(uidStory: String)

    // Stats
    suspend fun addLessonStatsToFirestore(flashcardUid: String, status: String)

    suspend fun fetchDataOfStats(): Pair<Statistics, StatsSummary>
}