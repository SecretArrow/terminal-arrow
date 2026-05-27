package com.terminalarrow.app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ConnectionProfile::class, Snippet::class, KnownHost::class],
    version = 4,
    exportSchema = false
)
@androidx.room.TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun terminalDao(): TerminalDao
}
