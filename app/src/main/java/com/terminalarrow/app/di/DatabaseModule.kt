package com.terminalarrow.app.di

import android.content.Context
import androidx.room.Room
import com.terminalarrow.app.data.AppDatabase
import com.terminalarrow.app.data.TerminalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "terminal_arrow_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideTerminalDao(database: AppDatabase): TerminalDao {
        return database.terminalDao()
    }
}
