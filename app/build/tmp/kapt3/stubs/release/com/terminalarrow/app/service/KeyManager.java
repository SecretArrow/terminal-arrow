package com.terminalarrow.app.service;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\bJ\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/terminalarrow/app/service/KeyManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "deleteKey", "", "alias", "", "generateKeyPair", "algorithm", "listKeys", "", "app_release"})
public final class KeyManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    @javax.inject.Inject()
    public KeyManager(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String generateKeyPair(@org.jetbrains.annotations.NotNull()
    java.lang.String alias, @org.jetbrains.annotations.NotNull()
    java.lang.String algorithm) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> listKeys() {
        return null;
    }
    
    public final void deleteKey(@org.jetbrains.annotations.NotNull()
    java.lang.String alias) {
    }
}