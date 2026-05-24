package com.terminalarrow.app.ui.cloud;

import com.terminalarrow.app.data.TerminalDao;
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
public final class CloudViewModel_Factory implements Factory<CloudViewModel> {
  private final Provider<TerminalDao> terminalDaoProvider;

  public CloudViewModel_Factory(Provider<TerminalDao> terminalDaoProvider) {
    this.terminalDaoProvider = terminalDaoProvider;
  }

  @Override
  public CloudViewModel get() {
    return newInstance(terminalDaoProvider.get());
  }

  public static CloudViewModel_Factory create(Provider<TerminalDao> terminalDaoProvider) {
    return new CloudViewModel_Factory(terminalDaoProvider);
  }

  public static CloudViewModel newInstance(TerminalDao terminalDao) {
    return new CloudViewModel(terminalDao);
  }
}
