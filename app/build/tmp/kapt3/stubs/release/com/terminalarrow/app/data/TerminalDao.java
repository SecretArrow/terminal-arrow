package com.terminalarrow.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\fH\'J\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u0011"}, d2 = {"Lcom/terminalarrow/app/data/TerminalDao;", "", "deleteProfile", "", "profile", "Lcom/terminalarrow/app/data/ConnectionProfile;", "(Lcom/terminalarrow/app/data/ConnectionProfile;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSnippet", "snippet", "Lcom/terminalarrow/app/data/Snippet;", "(Lcom/terminalarrow/app/data/Snippet;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllProfiles", "Lkotlinx/coroutines/flow/Flow;", "", "getAllSnippets", "insertProfile", "insertSnippet", "app_release"})
@androidx.room.Dao()
public abstract interface TerminalDao {
    
    @androidx.room.Query(value = "SELECT * FROM connection_profiles")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.terminalarrow.app.data.ConnectionProfile>> getAllProfiles();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertProfile(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteProfile(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM snippets")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.terminalarrow.app.data.Snippet>> getAllSnippets();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertSnippet(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.Snippet snippet, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteSnippet(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.Snippet snippet, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}