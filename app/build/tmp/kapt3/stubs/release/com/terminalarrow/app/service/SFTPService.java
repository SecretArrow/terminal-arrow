package com.terminalarrow.app.service;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J.\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0006\u0010\u0014\u001a\u00020\u0011J \u0010\u0015\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0012\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c2\u0006\u0010\u0012\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0013J \u0010\u001e\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0018J \u0010!\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0018R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/terminalarrow/app/service/SFTPService;", "", "()V", "client", "Lnet/schmizz/sshj/SSHClient;", "sftpClient", "Lnet/schmizz/sshj/sftp/SFTPClient;", "connect", "", "host", "", "port", "", "user", "pass", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteFile", "", "path", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "downloadFile", "remotePath", "localPath", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRemoteInputStream", "Ljava/io/InputStream;", "listFiles", "", "Lnet/schmizz/sshj/sftp/RemoteResourceInfo;", "renameFile", "oldPath", "newPath", "uploadFile", "app_release"})
public final class SFTPService {
    @org.jetbrains.annotations.Nullable()
    private net.schmizz.sshj.SSHClient client;
    @org.jetbrains.annotations.Nullable()
    private net.schmizz.sshj.sftp.SFTPClient sftpClient;
    
    @javax.inject.Inject()
    public SFTPService() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    java.lang.String host, int port, @org.jetbrains.annotations.NotNull()
    java.lang.String user, @org.jetbrains.annotations.NotNull()
    java.lang.String pass, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object listFiles(@org.jetbrains.annotations.NotNull()
    java.lang.String path, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<? extends net.schmizz.sshj.sftp.RemoteResourceInfo>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object downloadFile(@org.jetbrains.annotations.NotNull()
    java.lang.String remotePath, @org.jetbrains.annotations.NotNull()
    java.lang.String localPath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object uploadFile(@org.jetbrains.annotations.NotNull()
    java.lang.String localPath, @org.jetbrains.annotations.NotNull()
    java.lang.String remotePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteFile(@org.jetbrains.annotations.NotNull()
    java.lang.String path, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object renameFile(@org.jetbrains.annotations.NotNull()
    java.lang.String oldPath, @org.jetbrains.annotations.NotNull()
    java.lang.String newPath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getRemoteInputStream(@org.jetbrains.annotations.NotNull()
    java.lang.String path, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.io.InputStream> $completion) {
        return null;
    }
    
    public final void disconnect() {
    }
}