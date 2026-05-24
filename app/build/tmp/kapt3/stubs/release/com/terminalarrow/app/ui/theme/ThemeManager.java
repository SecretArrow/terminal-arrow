package com.terminalarrow.app.ui.theme;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020)R+\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00048F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\n\u0010\u000b\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR&\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R+\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0019\u0010\u000b\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R+\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0003\u001a\u00020\u001a8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b \u0010\u000b\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00040\"\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$\u00a8\u0006*"}, d2 = {"Lcom/terminalarrow/app/ui/theme/ThemeManager;", "", "()V", "<set-?>", "Lcom/terminalarrow/app/ui/theme/TerminalTheme;", "currentTheme", "getCurrentTheme", "()Lcom/terminalarrow/app/ui/theme/TerminalTheme;", "setCurrentTheme", "(Lcom/terminalarrow/app/ui/theme/TerminalTheme;)V", "currentTheme$delegate", "Landroidx/compose/runtime/MutableState;", "fontFamilies", "Landroidx/compose/runtime/snapshots/SnapshotStateMap;", "", "Landroidx/compose/ui/text/font/FontFamily;", "getFontFamilies", "()Landroidx/compose/runtime/snapshots/SnapshotStateMap;", "setFontFamilies", "(Landroidx/compose/runtime/snapshots/SnapshotStateMap;)V", "fontFamily", "getFontFamily", "()Landroidx/compose/ui/text/font/FontFamily;", "setFontFamily", "(Landroidx/compose/ui/text/font/FontFamily;)V", "fontFamily$delegate", "", "fontSize", "getFontSize", "()I", "setFontSize", "(I)V", "fontSize$delegate", "themes", "", "getThemes", "()Ljava/util/List;", "addCustomFont", "", "name", "file", "Ljava/io/File;", "app_release"})
public final class ThemeManager {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.terminalarrow.app.ui.theme.TerminalTheme> themes = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState currentTheme$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState fontSize$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState fontFamily$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.compose.runtime.snapshots.SnapshotStateMap<java.lang.String, androidx.compose.ui.text.font.FontFamily> fontFamilies;
    
    @javax.inject.Inject()
    public ThemeManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.terminalarrow.app.ui.theme.TerminalTheme> getThemes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.terminalarrow.app.ui.theme.TerminalTheme getCurrentTheme() {
        return null;
    }
    
    public final void setCurrentTheme(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.ui.theme.TerminalTheme p0) {
    }
    
    public final int getFontSize() {
        return 0;
    }
    
    public final void setFontSize(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.text.font.FontFamily getFontFamily() {
        return null;
    }
    
    public final void setFontFamily(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.font.FontFamily p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.runtime.snapshots.SnapshotStateMap<java.lang.String, androidx.compose.ui.text.font.FontFamily> getFontFamilies() {
        return null;
    }
    
    public final void setFontFamilies(@org.jetbrains.annotations.NotNull()
    androidx.compose.runtime.snapshots.SnapshotStateMap<java.lang.String, androidx.compose.ui.text.font.FontFamily> p0) {
    }
    
    public final void addCustomFont(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.io.File file) {
    }
}