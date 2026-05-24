package com.terminalarrow.app.di;

import com.terminalarrow.app.data.AppDatabase;
import com.terminalarrow.app.data.TerminalDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideTerminalDaoFactory implements Factory<TerminalDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTerminalDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TerminalDao get() {
    return provideTerminalDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTerminalDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTerminalDaoFactory(databaseProvider);
  }

  public static TerminalDao provideTerminalDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTerminalDao(database));
  }
}
