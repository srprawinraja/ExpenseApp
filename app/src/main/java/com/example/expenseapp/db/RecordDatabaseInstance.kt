package com.example.basicapp.db

import RecordDatabase
import android.content.Context
import androidx.room.Room

class RecordDatabaseInstance {
    companion object {
        private var INSTANCE: RecordDatabase? = null
        fun getInstance(context: Context): RecordDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                RecordDatabase::class.java,
                "app_db"
            ).build().also { INSTANCE = it }
        }
    }
}