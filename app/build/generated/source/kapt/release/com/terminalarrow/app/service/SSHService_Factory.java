package com.terminalarrow.app.service;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SSHService_Factory implements Factory<SSHService> {
  @Override
  public SSHService get() {
    return newInstance();
  }

  public static SSHService_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SSHService newInstance() {
    return new SSHService();
  }

  private static final class InstanceHolder {
    private static final SSHService_Factory INSTANCE = new SSHService_Factory();
  }
}
