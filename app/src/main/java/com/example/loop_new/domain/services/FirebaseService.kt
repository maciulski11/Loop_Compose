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

    // Flashcards
    fun createNewGoogleUser()

    fun getSignedInUser(): User?

    fun addPublicBoxToPrivateSection(boxUid: String)

    fun createBoxInPrivateSection(box: Box)

    fun deleteBox(boxUid: String)

    fun addFlashcardInPrivateSection(flashcard: Flashcard, boxUid: String)

    fun deleteFlashcard(boxUid: String, flashcardUid: String)

    fun fetchListOfPublicBox(lastDocSnapshot: DocumentSnapshot?): Flow<Pair<List<Box>, DocumentSnapshot?>>

    fun fetchListOfPrivateBox(): Flow<List<Box>>

    fun fetchListOfFlashcardInPublicBox(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInPrivateBox(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInLesson(boxUid: String): Flow<List<Flashcard>>

    fun fetchListOfFlashcardInRepeat(): Flow<List<Flashcard>>

//    fun addFlashcardsToRepeatSection()

    fun deleteFlashcardFromRepeatSection(flashcardUid: String)

    fun updateFlashcardToKnow(boxUid: String, flashcardUid: String)

    fun updateFlashcardToSomewhatKnow(boxUid: String, flashcardUid: String)

    fun updateFlashcardToDoNotKnow(boxUid: String, flashcardUid: String)

    fun setupRepeatCollectionListener(onCollectionUpdate: (Boolean) -> Unit)

    suspend fun checkRepeatCollectionWhetherIsEmpty(): Boolean

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