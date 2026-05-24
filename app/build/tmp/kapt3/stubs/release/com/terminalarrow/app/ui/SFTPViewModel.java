package com.terminalarrow.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u0007J\u000e\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u0007J\u000e\u0010!\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u0007J\u000e\u0010\"\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u0007J\u0006\u0010$\u001a\u00020\u0019J\b\u0010%\u001a\u00020\u0019H\u0014J\u0006\u0010&\u001a\u00020\u0019J\u0016\u0010\'\u001a\u00020\u00192\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001d\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/terminalarrow/app/ui/SFTPViewModel;", "Landroidx/lifecycle/ViewModel;", "sftpService", "Lcom/terminalarrow/app/service/SFTPService;", "(Lcom/terminalarrow/app/service/SFTPService;)V", "_currentPath", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_files", "", "Lcom/terminalarrow/app/ui/VirtualFile;", "_isInsideArchive", "", "_isLoading", "archiveContentsCache", "", "currentPath", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentPath", "()Lkotlinx/coroutines/flow/StateFlow;", "files", "getFiles", "isInsideArchive", "isLoading", "connectAndList", "", "host", "port", "", "user", "pass", "deleteFile", "path", "loadPath", "navigateIntoArchive", "archivePath", "navigateUp", "onCleared", "refresh", "renameFile", "oldPath", "newName", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SFTPViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.service.SFTPService sftpService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.terminalarrow.app.ui.VirtualFile>> _files = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.ui.VirtualFile>> files = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _currentPath = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> currentPath = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isInsideArchive = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isInsideArchive = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.String, ? extends java.util.List<com.terminalarrow.app.ui.VirtualFile>> archiveContentsCache;
    
    @javax.inject.Inject()
    public SFTPViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.service.SFTPService sftpService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.ui.VirtualFile>> getFiles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getCurrentPath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isInsideArchive() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    public final void connectAndList(@org.jetbrains.annotations.NotNull()
    java.lang.String host, int port, @org.jetbrains.annotations.NotNull()
    java.lang.String user, @org.jetbrains.annotations.NotNull()
    java.lang.String pass) {
    }
    
    public final void loadPath(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
    }
    
    public final void navigateIntoArchive(@org.jetbrains.annotations.NotNull()
    java.lang.String archivePath) {
    }
    
    public final void navigateUp() {
    }
    
    public final void deleteFile(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
    }
    
    public final void renameFile(@org.jetbrains.annotations.NotNull()
    java.lang.String oldPath, @org.jetbrains.annotations.NotNull()
    java.lang.String newName) {
    }
    
    public final void refresh() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}