package com.task.noteapp.entity.local

import com.task.noteapp.dao.NoteDao
import com.task.noteapp.entity.Note
import javax.inject.Inject

class LocalDataSourcesRoomBasedImpl @Inject constructor(private val noteDao: NoteDao) :
    LocalDataSource {

    override suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override suspend fun editNote(note: Note) {
        noteDao.editNote(note)
    }

    override fun getAllNotes(): List<Note> = noteDao.getAllNote()

    override fun getNote(id: Int): Note = noteDao.getNote(id)
}

