package com.terminalarrow.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u0018J\"\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001e2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00180 J\b\u0010!\u001a\u00020\u0018H\u0014J\u000e\u0010\"\u001a\u00020\u00182\u0006\u0010#\u001a\u00020\u000bJ\u000e\u0010$\u001a\u00020\u00182\u0006\u0010%\u001a\u00020\u000bJ\u000e\u0010&\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020\u000bJ\u000e\u0010(\u001a\u00020\u00182\u0006\u0010)\u001a\u00020\u000bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\r0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/terminalarrow/app/ui/TerminalViewModel;", "Landroidx/lifecycle/ViewModel;", "sshService", "Lcom/terminalarrow/app/service/SSHService;", "vibratorHelper", "Lcom/terminalarrow/app/utils/VibratorHelper;", "nativeProcessor", "Lcom/terminalarrow/app/utils/NativeBufferProcessor;", "(Lcom/terminalarrow/app/service/SSHService;Lcom/terminalarrow/app/utils/VibratorHelper;Lcom/terminalarrow/app/utils/NativeBufferProcessor;)V", "_searchQuery", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_suggestions", "", "_terminalOutput", "searchQuery", "Lkotlinx/coroutines/flow/StateFlow;", "getSearchQuery", "()Lkotlinx/coroutines/flow/StateFlow;", "suggestions", "getSuggestions", "terminalOutput", "getTerminalOutput", "connect", "", "profile", "Lcom/terminalarrow/app/data/ConnectionProfile;", "connectLocal", "exportTerminalOutput", "context", "Landroid/content/Context;", "onComplete", "Lkotlin/Function1;", "onCleared", "onInputChange", "text", "onSpecialKey", "key", "sendCommand", "command", "setSearchQuery", "query", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TerminalViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.service.SSHService sshService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.utils.VibratorHelper vibratorHelper = null;
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.utils.NativeBufferProcessor nativeProcessor = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _terminalOutput = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> terminalOutput = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<java.lang.String>> _suggestions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> suggestions = null;
    
    @javax.inject.Inject()
    public TerminalViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.service.SSHService sshService, @org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.utils.VibratorHelper vibratorHelper, @org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.utils.NativeBufferProcessor nativeProcessor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getTerminalOutput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> getSuggestions() {
        return null;
    }
    
    public final void connect(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile) {
    }
    
    public final void setSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void connectLocal() {
    }
    
    public final void onInputChange(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void sendCommand(@org.jetbrains.annotations.NotNull()
    java.lang.String command) {
    }
    
    public final void onSpecialKey(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final void exportTerminalOutput(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onComplete) {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}