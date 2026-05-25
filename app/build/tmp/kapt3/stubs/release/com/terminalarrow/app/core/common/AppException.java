package com.terminalarrow.app.core.common;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00060\u0001j\u0002`\u0002:\u0004\b\t\n\u000bB\u001b\b\u0004\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007\u0082\u0001\u0004\f\r\u000e\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/terminalarrow/app/core/common/AppException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "message", "", "cause", "", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "AuthException", "NetworkException", "ServerException", "UnknownException", "Lcom/terminalarrow/app/core/common/AppException$AuthException;", "Lcom/terminalarrow/app/core/common/AppException$NetworkException;", "Lcom/terminalarrow/app/core/common/AppException$ServerException;", "Lcom/terminalarrow/app/core/common/AppException$UnknownException;", "app_release"})
public abstract class AppException extends java.lang.Exception {
    
    private AppException(java.lang.String message, java.lang.Throwable cause) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/terminalarrow/app/core/common/AppException$AuthException;", "Lcom/terminalarrow/app/core/common/AppException;", "message", "", "(Ljava/lang/String;)V", "app_release"})
    public static final class AuthException extends com.terminalarrow.app.core.common.AppException {
        
        public AuthException(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/terminalarrow/app/core/common/AppException$NetworkException;", "Lcom/terminalarrow/app/core/common/AppException;", "cause", "", "(Ljava/lang/Throwable;)V", "app_release"})
    public static final class NetworkException extends com.terminalarrow.app.core.common.AppException {
        
        public NetworkException(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable cause) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/terminalarrow/app/core/common/AppException$ServerException;", "Lcom/terminalarrow/app/core/common/AppException;", "code", "", "message", "", "(ILjava/lang/String;)V", "getCode", "()I", "app_release"})
    public static final class ServerException extends com.terminalarrow.app.core.common.AppException {
        private final int code = 0;
        
        public ServerException(int code, @org.jetbrains.annotations.NotNull()
        java.lang.String message) {
        }
        
        public final int getCode() {
            return 0;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/terminalarrow/app/core/common/AppException$UnknownException;", "Lcom/terminalarrow/app/core/common/AppException;", "cause", "", "(Ljava/lang/Throwable;)V", "app_release"})
    public static final class UnknownException extends com.terminalarrow.app.core.common.AppException {
        
        public UnknownException(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable cause) {
        }
    }
}