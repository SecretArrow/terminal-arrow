package com.terminalarrow.app.service;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J*\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\b0\fH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\rR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/terminalarrow/app/service/SSHService;", "", "()V", "client", "Lnet/schmizz/sshj/SSHClient;", "shellStream", "Ljava/io/OutputStream;", "connect", "", "profile", "Lcom/terminalarrow/app/data/ConnectionProfile;", "onOutput", "Lkotlin/Function1;", "", "(Lcom/terminalarrow/app/data/ConnectionProfile;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "sendCommand", "command", "app_release"})
public final class SSHService {
    @org.jetbrains.annotations.Nullable()
    private net.schmizz.sshj.SSHClient client;
    @org.jetbrains.annotations.Nullable()
    private java.io.OutputStream shellStream;
    
    @javax.inject.Inject()
    public SSHService() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.ConnectionProfile profile, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOutput, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void sendCommand(@org.jetbrains.annotations.NotNull()
    java.lang.String command) {
    }
    
    public final void disconnect() {
    }
}