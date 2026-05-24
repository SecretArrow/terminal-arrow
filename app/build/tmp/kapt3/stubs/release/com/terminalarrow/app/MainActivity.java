package com.terminalarrow.app;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u0010H\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0014"}, d2 = {"Lcom/terminalarrow/app/MainActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "biometricHelper", "Lcom/terminalarrow/app/utils/BiometricHelper;", "getBiometricHelper", "()Lcom/terminalarrow/app/utils/BiometricHelper;", "setBiometricHelper", "(Lcom/terminalarrow/app/utils/BiometricHelper;)V", "themeManager", "Lcom/terminalarrow/app/ui/theme/ThemeManager;", "getThemeManager", "()Lcom/terminalarrow/app/ui/theme/ThemeManager;", "setThemeManager", "(Lcom/terminalarrow/app/ui/theme/ThemeManager;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setupContent", "app_release"})
public final class MainActivity extends androidx.fragment.app.FragmentActivity {
    @javax.inject.Inject()
    public com.terminalarrow.app.utils.BiometricHelper biometricHelper;
    @javax.inject.Inject()
    public com.terminalarrow.app.ui.theme.ThemeManager themeManager;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.terminalarrow.app.utils.BiometricHelper getBiometricHelper() {
        return null;
    }
    
    public final void setBiometricHelper(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.utils.BiometricHelper p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.terminalarrow.app.ui.theme.ThemeManager getThemeManager() {
        return null;
    }
    
    public final void setThemeManager(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.ui.theme.ThemeManager p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupContent() {
    }
}