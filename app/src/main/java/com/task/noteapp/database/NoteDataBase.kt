package com.task.noteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.task.noteapp.dao.NoteDao
import com.task.noteapp.entity.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var notesDataBase: NoteDataBase? = null

        @Synchronized
        fun getDataBase(context: Context): NoteDataBase {
            if (notesDataBase == null) {
                notesDataBase =
                    Room.databaseBuilder(context, NoteDataBase::class.java, "note.db").build()
            }
            return notesDataBase!!
        }
    }

}