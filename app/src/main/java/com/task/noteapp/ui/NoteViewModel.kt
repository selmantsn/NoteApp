package com.task.noteapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.entity.Note
import com.task.noteapp.entity.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) :
    ViewModel() {

    val allNoteListData = MutableLiveData<List<Note>>()
    val noteData = MutableLiveData<Note>()


    fun getAllNotes() {
        allNoteListData.value = noteRepository.getAllNotes()
    }

    fun getNote(id: Int) {
        noteData.value = noteRepository.getNote(id)
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.addNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.deleteNote(note)
    }

    fun editNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.editNote(note)
    }


}