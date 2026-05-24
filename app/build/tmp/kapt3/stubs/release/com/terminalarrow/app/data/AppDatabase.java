package com.terminalarrow.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcom/terminalarrow/app/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "terminalDao", "Lcom/terminalarrow/app/data/TerminalDao;", "app_release"})
@androidx.room.Database(entities = {com.terminalarrow.app.data.ConnectionProfile.class, com.terminalarrow.app.data.Snippet.class}, version = 2, exportSchema = false)
@androidx.room.TypeConverters(value = {com.terminalarrow.app.data.Converters.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.terminalarrow.app.data.TerminalDao terminalDao();
}