package com.example.loop_new.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.loop_new.domain.model.firebase.Box
import com.example.loop_new.domain.model.firebase.Flashcard
import com.example.loop_new.domain.model.firebase.RepeatSection

@Database(entities = [Flashcard::class, Box::class, RepeatSection::class], version = 2)
abstract class LoopDatabase : RoomDatabase() {

    abstract fun boxDao(): BoxDao
    abstract fun flashcardDao(): FlashcardDao

    abstract fun repeatSectionDao(): RepeatSectionDao

    companion object {
        @Volatile
        private var instance: LoopDatabase? = null

        fun getInstance(application: Application): LoopDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(application).also { instance = it }
            }
        }

        private fun buildDatabase(application: Application): LoopDatabase {
            return databaseBuilder(
                application,
                LoopDatabase::class.java,
                "loop_databaseXXpp"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}