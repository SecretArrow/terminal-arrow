package com.terminalarrow.app.ui.snippets;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u0016\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/terminalarrow/app/ui/snippets/SnippetViewModel;", "Landroidx/lifecycle/ViewModel;", "terminalDao", "Lcom/terminalarrow/app/data/TerminalDao;", "(Lcom/terminalarrow/app/data/TerminalDao;)V", "snippets", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/terminalarrow/app/data/Snippet;", "getSnippets", "()Lkotlinx/coroutines/flow/StateFlow;", "deleteSnippet", "", "snippet", "saveSnippet", "name", "", "command", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SnippetViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.data.TerminalDao terminalDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.data.Snippet>> snippets = null;
    
    @javax.inject.Inject()
    public SnippetViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.TerminalDao terminalDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.data.Snippet>> getSnippets() {
        return null;
    }
    
    public final void saveSnippet(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String command) {
    }
    
    public final void deleteSnippet(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.Snippet snippet) {
    }
}