package com.terminalarrow.app.service;

import android.content.Context;
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
public final class SSHService_Factory implements Factory<SSHService> {
  private final Provider<Context> contextProvider;

  public SSHService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SSHService get() {
    return newInstance(contextProvider.get());
  }

  public static SSHService_Factory create(Provider<Context> contextProvider) {
    return new SSHService_Factory(contextProvider);
  }

  public static SSHService newInstance(Context context) {
    return new SSHService(context);
  }
}
