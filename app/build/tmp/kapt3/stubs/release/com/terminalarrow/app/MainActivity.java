package com.terminalarrow.app;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u0016H\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001a"}, d2 = {"Lcom/terminalarrow/app/MainActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "biometricHelper", "Lcom/terminalarrow/app/utils/BiometricHelper;", "getBiometricHelper", "()Lcom/terminalarrow/app/utils/BiometricHelper;", "setBiometricHelper", "(Lcom/terminalarrow/app/utils/BiometricHelper;)V", "themeManager", "Lcom/terminalarrow/app/ui/theme/ThemeManager;", "getThemeManager", "()Lcom/terminalarrow/app/ui/theme/ThemeManager;", "setThemeManager", "(Lcom/terminalarrow/app/ui/theme/ThemeManager;)V", "vibratorHelper", "Lcom/terminalarrow/app/utils/VibratorHelper;", "getVibratorHelper", "()Lcom/terminalarrow/app/utils/VibratorHelper;", "setVibratorHelper", "(Lcom/terminalarrow/app/utils/VibratorHelper;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setupContent", "app_release"})
public final class MainActivity extends androidx.fragment.app.FragmentActivity {
    @javax.inject.Inject()
    public com.terminalarrow.app.utils.BiometricHelper biometricHelper;
    @javax.inject.Inject()
    public com.terminalarrow.app.ui.theme.ThemeManager themeManager;
    @javax.inject.Inject()
    public com.terminalarrow.app.utils.VibratorHelper vibratorHelper;
    
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
    
    @org.jetbrains.annotations.NotNull()
    public final com.terminalarrow.app.utils.VibratorHelper getVibratorHelper() {
        return null;
    }
    
    public final void setVibratorHelper(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.utils.VibratorHelper p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupContent() {
    }
}