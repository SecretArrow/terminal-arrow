package com.terminalarrow.app.ui;

import com.terminalarrow.app.service.SFTPService;
import com.terminalarrow.app.service.SSHService;
import com.terminalarrow.app.utils.NativeBufferProcessor;
import com.terminalarrow.app.utils.VibratorHelper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class TerminalViewModel_Factory implements Factory<TerminalViewModel> {
  private final Provider<SSHService> sshServiceProvider;

  private final Provider<SFTPService> sftpServiceProvider;

  private final Provider<VibratorHelper> vibratorHelperProvider;

  private final Provider<NativeBufferProcessor> nativeProcessorProvider;

  public TerminalViewModel_Factory(Provider<SSHService> sshServiceProvider,
      Provider<SFTPService> sftpServiceProvider, Provider<VibratorHelper> vibratorHelperProvider,
      Provider<NativeBufferProcessor> nativeProcessorProvider) {
    this.sshServiceProvider = sshServiceProvider;
    this.sftpServiceProvider = sftpServiceProvider;
    this.vibratorHelperProvider = vibratorHelperProvider;
    this.nativeProcessorProvider = nativeProcessorProvider;
  }

  @Override
  public TerminalViewModel get() {
    return newInstance(sshServiceProvider.get(), sftpServiceProvider.get(), vibratorHelperProvider.get(), nativeProcessorProvider.get());
  }

  public static TerminalViewModel_Factory create(Provider<SSHService> sshServiceProvider,
      Provider<SFTPService> sftpServiceProvider, Provider<VibratorHelper> vibratorHelperProvider,
      Provider<NativeBufferProcessor> nativeProcessorProvider) {
    return new TerminalViewModel_Factory(sshServiceProvider, sftpServiceProvider, vibratorHelperProvider, nativeProcessorProvider);
  }

  public static TerminalViewModel newInstance(SSHService sshService, SFTPService sftpService,
      VibratorHelper vibratorHelper, NativeBufferProcessor nativeProcessor) {
    return new TerminalViewModel(sshService, sftpService, vibratorHelper, nativeProcessor);
  }
}
