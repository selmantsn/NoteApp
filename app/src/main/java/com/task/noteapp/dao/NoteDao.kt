package com.task.noteapp.dao

import androidx.room.*
import com.task.noteapp.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNote(): List<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNote(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun editNote(note: Note)
}
