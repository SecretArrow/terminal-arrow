package com.terminalarrow.app.utils;

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
public final class NativeBufferProcessor_Factory implements Factory<NativeBufferProcessor> {
  @Override
  public NativeBufferProcessor get() {
    return newInstance();
  }

  public static NativeBufferProcessor_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NativeBufferProcessor newInstance() {
    return new NativeBufferProcessor();
  }

  private static final class InstanceHolder {
    private static final NativeBufferProcessor_Factory INSTANCE = new NativeBufferProcessor_Factory();
  }
}
