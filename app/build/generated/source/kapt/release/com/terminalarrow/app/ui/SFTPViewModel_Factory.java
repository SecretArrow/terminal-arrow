package com.terminalarrow.app.ui;

import com.terminalarrow.app.service.SFTPService;
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
public final class SFTPViewModel_Factory implements Factory<SFTPViewModel> {
  private final Provider<SFTPService> sftpServiceProvider;

  public SFTPViewModel_Factory(Provider<SFTPService> sftpServiceProvider) {
    this.sftpServiceProvider = sftpServiceProvider;
  }

  @Override
  public SFTPViewModel get() {
    return newInstance(sftpServiceProvider.get());
  }

  public static SFTPViewModel_Factory create(Provider<SFTPService> sftpServiceProvider) {
    return new SFTPViewModel_Factory(sftpServiceProvider);
  }

  public static SFTPViewModel newInstance(SFTPService sftpService) {
    return new SFTPViewModel(sftpService);
  }
}
