package com.task.noteapp.di

import com.task.noteapp.dao.NoteDao
import com.task.noteapp.entity.NoteRepository
import com.task.noteapp.entity.local.LocalDataSource
import com.task.noteapp.entity.local.LocalDataSourcesRoomBasedImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(localDataSource: LocalDataSource) = NoteRepository(localDataSource)

    @Provides
    @Singleton
    fun provideIOfflineDataSource(noteDao: NoteDao) =
        LocalDataSourcesRoomBasedImpl(noteDao) as LocalDataSource


}