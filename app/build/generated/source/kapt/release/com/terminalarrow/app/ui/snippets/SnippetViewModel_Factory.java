package com.terminalarrow.app.ui.snippets;

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
public final class SnippetViewModel_Factory implements Factory<SnippetViewModel> {
  private final Provider<TerminalDao> terminalDaoProvider;

  public SnippetViewModel_Factory(Provider<TerminalDao> terminalDaoProvider) {
    this.terminalDaoProvider = terminalDaoProvider;
  }

  @Override
  public SnippetViewModel get() {
    return newInstance(terminalDaoProvider.get());
  }

  public static SnippetViewModel_Factory create(Provider<TerminalDao> terminalDaoProvider) {
    return new SnippetViewModel_Factory(terminalDaoProvider);
  }

  public static SnippetViewModel newInstance(TerminalDao terminalDao) {
    return new SnippetViewModel(terminalDao);
  }
}
