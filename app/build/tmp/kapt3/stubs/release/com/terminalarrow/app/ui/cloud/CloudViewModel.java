package com.terminalarrow.app.ui.cloud;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010J\u000e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0010J\u000e\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\bR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/terminalarrow/app/ui/cloud/CloudViewModel;", "Landroidx/lifecycle/ViewModel;", "terminalDao", "Lcom/terminalarrow/app/data/TerminalDao;", "(Lcom/terminalarrow/app/data/TerminalDao;)V", "_instances", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/terminalarrow/app/ui/cloud/CloudInstance;", "instances", "Lkotlinx/coroutines/flow/StateFlow;", "getInstances", "()Lkotlinx/coroutines/flow/StateFlow;", "fetchAWSInstances", "", "accessKey", "", "secretKey", "region", "fetchDigitalOceanInstances", "token", "importInstance", "instance", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CloudViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.data.TerminalDao terminalDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.terminalarrow.app.ui.cloud.CloudInstance>> _instances = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.ui.cloud.CloudInstance>> instances = null;
    
    @javax.inject.Inject()
    public CloudViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.TerminalDao terminalDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.terminalarrow.app.ui.cloud.CloudInstance>> getInstances() {
        return null;
    }
    
    public final void fetchAWSInstances(@org.jetbrains.annotations.NotNull()
    java.lang.String accessKey, @org.jetbrains.annotations.NotNull()
    java.lang.String secretKey, @org.jetbrains.annotations.NotNull()
    java.lang.String region) {
    }
    
    public final void fetchDigitalOceanInstances(@org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    public final void importInstance(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.ui.cloud.CloudInstance instance) {
    }
}