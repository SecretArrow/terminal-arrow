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
public final class EditorViewModel_Factory implements Factory<EditorViewModel> {
  private final Provider<SFTPService> sftpServiceProvider;

  public EditorViewModel_Factory(Provider<SFTPService> sftpServiceProvider) {
    this.sftpServiceProvider = sftpServiceProvider;
  }

  @Override
  public EditorViewModel get() {
    return newInstance(sftpServiceProvider.get());
  }

  public static EditorViewModel_Factory create(Provider<SFTPService> sftpServiceProvider) {
    return new EditorViewModel_Factory(sftpServiceProvider);
  }

  public static EditorViewModel newInstance(SFTPService sftpService) {
    return new EditorViewModel(sftpService);
  }
}
