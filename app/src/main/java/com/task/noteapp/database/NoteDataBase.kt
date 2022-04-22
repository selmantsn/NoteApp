package com.task.noteapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.noteapp.dao.NoteDao
import com.task.noteapp.entity.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}