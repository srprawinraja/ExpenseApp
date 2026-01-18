package com.example.expenseapp.db.record

import RecordDatabase
import android.content.Context
import com.example.basicapp.db.RecordDatabaseInstance

class RecordRepository (context: Context){
    private val db = RecordDatabaseInstance.getInstance(context)
    private val recordDao = db.recordDao()

}