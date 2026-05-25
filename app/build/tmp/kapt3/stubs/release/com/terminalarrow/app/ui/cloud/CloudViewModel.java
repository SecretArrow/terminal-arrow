package com.terminalarrow.app.ui.cloud;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0016H\u0002J\u0010\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u0016H\u0002J\u0010\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u000e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020 R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006!"}, d2 = {"Lcom/terminalarrow/app/ui/cloud/CloudViewModel;", "Landroidx/lifecycle/ViewModel;", "terminalDao", "Lcom/terminalarrow/app/data/TerminalDao;", "(Lcom/terminalarrow/app/data/TerminalDao;)V", "_uiEffect", "Lkotlinx/coroutines/channels/Channel;", "Lcom/terminalarrow/app/feature/cloud/CloudUiEffect;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/terminalarrow/app/feature/cloud/CloudUiState;", "uiEffect", "Lkotlinx/coroutines/flow/Flow;", "getUiEffect", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "fetchAWSInstances", "", "accessKey", "", "secretKey", "region", "fetchDigitalOceanInstances", "token", "importInstance", "instance", "Lcom/terminalarrow/app/ui/cloud/CloudInstance;", "onEvent", "event", "Lcom/terminalarrow/app/feature/cloud/CloudUiEvent;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CloudViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.terminalarrow.app.data.TerminalDao terminalDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.terminalarrow.app.feature.cloud.CloudUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.terminalarrow.app.feature.cloud.CloudUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.terminalarrow.app.feature.cloud.CloudUiEffect> _uiEffect = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.terminalarrow.app.feature.cloud.CloudUiEffect> uiEffect = null;
    
    @javax.inject.Inject()
    public CloudViewModel(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.data.TerminalDao terminalDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.terminalarrow.app.feature.cloud.CloudUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.terminalarrow.app.feature.cloud.CloudUiEffect> getUiEffect() {
        return null;
    }
    
    public final void onEvent(@org.jetbrains.annotations.NotNull()
    com.terminalarrow.app.feature.cloud.CloudUiEvent event) {
    }
    
    private final void fetchAWSInstances(java.lang.String accessKey, java.lang.String secretKey, java.lang.String region) {
    }
    
    private final void fetchDigitalOceanInstances(java.lang.String token) {
    }
    
    private final void importInstance(com.terminalarrow.app.ui.cloud.CloudInstance instance) {
    }
}