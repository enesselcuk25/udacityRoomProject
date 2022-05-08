package com.eneselcuk.room.di

import android.app.Application
import com.eneselcuk.room.database.SleepDatabase
import com.eneselcuk.room.database.SleepDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideRepository(natDao: NoteDao)=NoteRepository(natDao)

    @Singleton
    @Provides
    fun provideDatabase(context: Application): SleepDatabase {
        return SleepDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDao(noteDatabase: SleepDatabase): SleepDatabaseDao {
        return noteDatabase.sleepDatabaseDao
    }
}