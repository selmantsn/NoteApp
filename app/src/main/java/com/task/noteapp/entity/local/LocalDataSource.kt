package com.task.noteapp.entity.local

import com.task.noteapp.entity.Note

interface LocalDataSource {

    fun getAllNotes(): List<Note>

    fun getNote(id: Int): Note

    suspend fun addNote(note: Note) {}

    suspend fun deleteNote(note: Note) {}

    suspend fun editNote(note: Note) {}
}