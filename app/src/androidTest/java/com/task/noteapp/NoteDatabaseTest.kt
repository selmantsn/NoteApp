package com.task.noteapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.dao.NoteDao
import com.task.noteapp.database.NoteDataBase
import com.task.noteapp.entity.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class NoteDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var noteDao: NoteDao
    private lateinit var noteDataBase: NoteDataBase

    @Before
    fun createDb() {
        noteDataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()

        noteDao = noteDataBase.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        noteDataBase.close()
    }


    @Test
    fun insertAndGetData() = runBlockingTest {
        val fakeNote = Note(
            id = 1,
            title = "title",
            description = "desc",
            date = "01/01/2022",
            imgUrl = "",
            edited = false
        )
        noteDao.addNote(fakeNote)
        val allNote = noteDao.getAllNote()
        assertThat(allNote).contains(fakeNote)
    }



    @Test
    fun deleteNote() = runBlockingTest {
        val fakeNote = Note(
            id = 1,
            title = "title",
            description = "desc",
            date = "01/01/2022",
            imgUrl = "",
            edited = false
        )
        noteDao.addNote(fakeNote)
        noteDao.deleteNote(fakeNote)
        val allUsers = noteDao.getAllNote()
        assertThat(allUsers).doesNotContain(fakeNote)
    }
}