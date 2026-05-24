package com.terminalarrow.app.utils;

import android.content.Context;
import com.terminalarrow.app.data.TerminalDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class BackupManager_Factory implements Factory<BackupManager> {
  private final Provider<Context> contextProvider;

  private final Provider<TerminalDao> terminalDaoProvider;

  public BackupManager_Factory(Provider<Context> contextProvider,
      Provider<TerminalDao> terminalDaoProvider) {
    this.contextProvider = contextProvider;
    this.terminalDaoProvider = terminalDaoProvider;
  }

  @Override
  public BackupManager get() {
    return newInstance(contextProvider.get(), terminalDaoProvider.get());
  }

  public static BackupManager_Factory create(Provider<Context> contextProvider,
      Provider<TerminalDao> terminalDaoProvider) {
    return new BackupManager_Factory(contextProvider, terminalDaoProvider);
  }

  public static BackupManager newInstance(Context context, TerminalDao terminalDao) {
    return new BackupManager(context, terminalDao);
  }
}
