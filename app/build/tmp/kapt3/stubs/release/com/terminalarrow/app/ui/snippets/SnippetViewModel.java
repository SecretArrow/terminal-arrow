package com.terminalarrow.app.ui.snippets;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017J\u0018\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001c"}, d2 = {"Lcom/terminalarrow/app/ui/snippets/SnippetViewModel;", "Landroidx/lifecycle/ViewModel;", "terminalDao", "Lcom/terminalarrow/app/data/TerminalDao;", "(Lcom/terminalarrow/app/data/TerminalDao;)V", "_uiEffect", "Lkotlinx/coroutines/channels/Channel;", "Lcom/terminalarrow/app/feature/snippets/SnippetsUiEffect;", "uiEffect", "Lkotlinx/coroutines/flow/Flow;", "getUiEffect", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/terminalarrow/app/feature/snippets/SnippetsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "deleteSnippet", "", "snippet", "Lcom/terminalarrow/app/data/Snippet;", "onEvent", "event", "Lcom/terminalarrow/app/feature/snippets/SnippetsUiEvent;", "saveSnippet", "name", "", "command", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SnippetViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.data.TerminalDao terminalDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.terminalarrow.app.feature.snippets.SnippetsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.terminalarrow.app.feature.snippets.SnippetsUiEffect> _uiEffect = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.terminalarrow.app.feature.snippets.SnippetsUiEffect> uiEffect = null;
    
    @javax.inject.Inject()
    public SnippetViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.TerminalDao terminalDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.terminalarrow.app.feature.snippets.SnippetsUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.terminalarrow.app.feature.snippets.SnippetsUiEffect> getUiEffect() {
        return null;
    }
    
    public final void onEvent(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.feature.snippets.SnippetsUiEvent event) {
    }
    
    private final void saveSnippet(java.lang.String name, java.lang.String command) {
    }
    
    private final void deleteSnippet(com.terminalarrow.app.data.Snippet snippet) {
    }
}