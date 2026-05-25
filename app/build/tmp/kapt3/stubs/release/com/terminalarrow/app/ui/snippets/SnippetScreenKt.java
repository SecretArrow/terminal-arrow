package com.terminalarrow.app.ui.snippets;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a0\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a>\u0010\u0007\u001a\u00020\u00012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\fH\u0003\u001a$\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\fH\u0007\u00a8\u0006\u0011"}, d2 = {"AddSnippetDialog", "", "onDismiss", "Lkotlin/Function0;", "onSave", "Lkotlin/Function2;", "", "SnippetList", "snippets", "", "Lcom/terminalarrow/app/data/Snippet;", "onSnippetUse", "Lkotlin/Function1;", "onDelete", "SnippetScreen", "viewModel", "Lcom/terminalarrow/app/ui/snippets/SnippetViewModel;", "app_release"})
public final class SnippetScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SnippetScreen(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.ui.snippets.SnippetViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSnippetUse) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SnippetList(java.util.List<com.terminalarrow.app.data.Snippet> snippets, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSnippetUse, kotlin.jvm.functions.Function1<? super com.terminalarrow.app.data.Snippet, kotlin.Unit> onDelete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AddSnippetDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onSave) {
    }
}