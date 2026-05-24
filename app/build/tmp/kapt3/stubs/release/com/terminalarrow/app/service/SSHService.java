package com.terminalarrow.app.service;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0016B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J2\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000f0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u0007J\u0006\u0010\u0012\u001a\u00020\u000fJ\u0006\u0010\u0013\u001a\u00020\u0003J\u0016\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/terminalarrow/app/service/SSHService;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "sessions", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/terminalarrow/app/service/SSHService$SessionContainer;", "connect", "sessionId", "profile", "Lcom/terminalarrow/app/data/ConnectionProfile;", "onOutput", "Lkotlin/Function1;", "", "(Ljava/lang/String;Lcom/terminalarrow/app/data/ConnectionProfile;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "disconnectAll", "getContext", "sendCommand", "command", "SessionContainer", "app_release"})
public final class SSHService {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.terminalarrow.app.service.SSHService.SessionContainer> sessions = null;
    
    @javax.inject.Inject()
    public SSHService(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getContext() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOutput, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
    
    public final void sendCommand(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    java.lang.String command) {
    }
    
    public final void disconnect(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId) {
    }
    
    public final void disconnectAll() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/terminalarrow/app/service/SSHService$SessionContainer;", "", "client", "Lnet/schmizz/sshj/SSHClient;", "shellStream", "Ljava/io/OutputStream;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "(Lnet/schmizz/sshj/SSHClient;Ljava/io/OutputStream;Lkotlinx/coroutines/CoroutineScope;)V", "getClient", "()Lnet/schmizz/sshj/SSHClient;", "getScope", "()Lkotlinx/coroutines/CoroutineScope;", "getShellStream", "()Ljava/io/OutputStream;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_release"})
    public static final class SessionContainer {
        @org.jetbrains.annotations.NotNull()
        private final net.schmizz.sshj.SSHClient client = null;
        @org.jetbrains.annotations.NotNull()
        private final java.io.OutputStream shellStream = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlinx.coroutines.CoroutineScope scope = null;
        
        public SessionContainer(@org.jetbrains.annotations.NotNull()
        net.schmizz.sshj.SSHClient client, @org.jetbrains.annotations.NotNull()
        java.io.OutputStream shellStream, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.CoroutineScope scope) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final net.schmizz.sshj.SSHClient getClient() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.io.OutputStream getShellStream() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.CoroutineScope getScope() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final net.schmizz.sshj.SSHClient component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.io.OutputStream component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.CoroutineScope component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.terminalarrow.app.service.SSHService.SessionContainer copy(@org.jetbrains.annotations.NotNull()
        net.schmizz.sshj.SSHClient client, @org.jetbrains.annotations.NotNull()
        java.io.OutputStream shellStream, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.CoroutineScope scope) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}