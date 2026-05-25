package com.terminalarrow.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J6\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0016H\u0002J\u0010\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0016H\u0002J\u0018\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\u0016H\u0002J\u0010\u0010!\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0016H\u0002J\u0016\u0010\"\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0016H\u0082@\u00a2\u0006\u0002\u0010#J\u0010\u0010$\u001a\u00020\u00142\u0006\u0010%\u001a\u00020\u0016H\u0002J\b\u0010&\u001a\u00020\u0014H\u0002J\b\u0010\'\u001a\u00020\u0014H\u0014J\u000e\u0010(\u001a\u00020\u00142\u0006\u0010)\u001a\u00020*J\b\u0010+\u001a\u00020\u0014H\u0002J\u0018\u0010,\u001a\u00020\u00142\u0006\u0010-\u001a\u00020\u00162\u0006\u0010.\u001a\u00020\u0016H\u0002J\u0018\u0010/\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\u0016H\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u00060"}, d2 = {"Lcom/terminalarrow/app/ui/SFTPViewModel;", "Landroidx/lifecycle/ViewModel;", "sftpService", "Lcom/terminalarrow/app/service/SFTPService;", "(Lcom/terminalarrow/app/service/SFTPService;)V", "_uiEffect", "Lkotlinx/coroutines/channels/Channel;", "Lcom/terminalarrow/app/feature/sftp/SftpUiEffect;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/terminalarrow/app/feature/sftp/SftpUiState;", "uiEffect", "Lkotlinx/coroutines/flow/Flow;", "getUiEffect", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "connectAndList", "", "host", "", "port", "", "user", "pass", "keyPath", "deleteFile", "path", "downloadFile", "remotePath", "localPath", "loadPath", "loadPathInternal", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "navigateIntoArchive", "archivePath", "navigateUp", "onCleared", "onEvent", "event", "Lcom/terminalarrow/app/feature/sftp/SftpUiEvent;", "refresh", "renameFile", "oldPath", "newPath", "uploadFile", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SFTPViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.service.SFTPService sftpService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.terminalarrow.app.feature.sftp.SftpUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.terminalarrow.app.feature.sftp.SftpUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.terminalarrow.app.feature.sftp.SftpUiEffect> _uiEffect = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.terminalarrow.app.feature.sftp.SftpUiEffect> uiEffect = null;
    
    @javax.inject.Inject()
    public SFTPViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.service.SFTPService sftpService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.terminalarrow.app.feature.sftp.SftpUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.terminalarrow.app.feature.sftp.SftpUiEffect> getUiEffect() {
        return null;
    }
    
    public final void onEvent(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.feature.sftp.SftpUiEvent event) {
    }
    
    private final void connectAndList(java.lang.String host, int port, java.lang.String user, java.lang.String pass, java.lang.String keyPath) {
    }
    
    private final void loadPath(java.lang.String path) {
    }
    
    private final java.lang.Object loadPathInternal(java.lang.String path, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void navigateIntoArchive(java.lang.String archivePath) {
    }
    
    private final void navigateUp() {
    }
    
    private final void downloadFile(java.lang.String remotePath, java.lang.String localPath) {
    }
    
    private final void uploadFile(java.lang.String localPath, java.lang.String remotePath) {
    }
    
    private final void deleteFile(java.lang.String path) {
    }
    
    private final void renameFile(java.lang.String oldPath, java.lang.String newPath) {
    }
    
    private final void refresh() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}