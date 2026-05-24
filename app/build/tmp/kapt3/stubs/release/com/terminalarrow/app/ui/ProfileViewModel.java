package com.terminalarrow.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nJ\u001a\u0010\u0010\u001a\u00020\u000e2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u000e0\u0012J\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0013J\u000e\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/terminalarrow/app/ui/ProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "terminalDao", "Lcom/terminalarrow/app/data/TerminalDao;", "backupManager", "Lcom/terminalarrow/app/utils/BackupManager;", "(Lcom/terminalarrow/app/data/TerminalDao;Lcom/terminalarrow/app/utils/BackupManager;)V", "profiles", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/terminalarrow/app/data/ConnectionProfile;", "getProfiles", "()Lkotlinx/coroutines/flow/StateFlow;", "deleteProfile", "", "profile", "exportData", "onComplete", "Lkotlin/Function1;", "", "importData", "json", "saveProfile", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ProfileViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.data.TerminalDao terminalDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.utils.BackupManager backupManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.data.ConnectionProfile>> profiles = null;
    
    @javax.inject.Inject()
    public ProfileViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.TerminalDao terminalDao, @org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.utils.BackupManager backupManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.data.ConnectionProfile>> getProfiles() {
        return null;
    }
    
    public final void exportData(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onComplete) {
    }
    
    public final void importData(@org.jetbrains.annotations.NotNull()
    java.lang.String json) {
    }
    
    public final void saveProfile(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile) {
    }
    
    public final void deleteProfile(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile) {
    }
}