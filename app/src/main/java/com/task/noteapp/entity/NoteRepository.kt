package com.task.noteapp.entity

import com.task.noteapp.entity.local.LocalDataSource
import javax.inject.Inject

class NoteRepository @Inject constructor(private val localDataSource: LocalDataSource) {

    fun getAllNotes(): List<Note> = localDataSource.getAllNotes()

    fun getNote(id: Int): Note = localDataSource.getNote(id)

    suspend fun addNote(note: Note) {
        localDataSource.addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        localDataSource.deleteNote(note)
    }

    suspend fun editNote(note: Note) {
        localDataSource.editNote(note)
    }

}