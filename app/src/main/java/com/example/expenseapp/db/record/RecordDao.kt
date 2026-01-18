package com.example.expenseapp.db.record

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface RecordDao {
    @Insert
    suspend fun insertAllRecord(recordEntity: List<RecordEntity>)

    @Query("SELECT * FROM RecordEntity")
    suspend fun getAllRecord(): List<RecordEntity>

    @Query("DELETE FROM RecordEntity")
    suspend fun clearRecords()

}