package com.terminalarrow.app.utils;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J0\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\b0\f\u00a8\u0006\u000e"}, d2 = {"Lcom/terminalarrow/app/utils/BiometricHelper;", "", "()V", "isBiometricAvailable", "", "activity", "Landroidx/fragment/app/FragmentActivity;", "showBiometricPrompt", "", "onSuccess", "Lkotlin/Function0;", "onError", "Lkotlin/Function1;", "", "app_release"})
public final class BiometricHelper {
    
    @javax.inject.Inject()
    public BiometricHelper() {
        super();
    }
    
    public final void showBiometricPrompt(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError) {
    }
    
    public final boolean isBiometricAvailable(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity) {
        return false;
    }
}