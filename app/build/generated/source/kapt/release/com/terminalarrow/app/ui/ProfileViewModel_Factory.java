package com.terminalarrow.app.ui;

import com.terminalarrow.app.data.TerminalDao;
import com.terminalarrow.app.utils.BackupManager;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<TerminalDao> terminalDaoProvider;

  private final Provider<BackupManager> backupManagerProvider;

  public ProfileViewModel_Factory(Provider<TerminalDao> terminalDaoProvider,
      Provider<BackupManager> backupManagerProvider) {
    this.terminalDaoProvider = terminalDaoProvider;
    this.backupManagerProvider = backupManagerProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(terminalDaoProvider.get(), backupManagerProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<TerminalDao> terminalDaoProvider,
      Provider<BackupManager> backupManagerProvider) {
    return new ProfileViewModel_Factory(terminalDaoProvider, backupManagerProvider);
  }

  public static ProfileViewModel newInstance(TerminalDao terminalDao, BackupManager backupManager) {
    return new ProfileViewModel(terminalDao, backupManager);
  }
}
