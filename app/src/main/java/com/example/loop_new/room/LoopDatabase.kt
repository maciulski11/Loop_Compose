package com.example.loop_new.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.loop_new.domain.model.firebase.Box

@Database(entities = [Box::class], version = 1)
abstract class LoopDatabase : RoomDatabase() {

    abstract fun loopDao(): LoopDao

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
                "database"
            ).build()
        }
    }
}