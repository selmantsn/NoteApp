package com.task.noteapp.di

import android.content.Context
import androidx.room.Room
import com.task.noteapp.database.NoteDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NoteDataBase::class.java, "note.db"
    ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideUserDao(db: NoteDataBase) = db.noteDao()


}
