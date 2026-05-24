package com.terminalarrow.app.utils;

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
public final class VibratorHelper_Factory implements Factory<VibratorHelper> {
  private final Provider<Context> contextProvider;

  public VibratorHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VibratorHelper get() {
    return newInstance(contextProvider.get());
  }

  public static VibratorHelper_Factory create(Provider<Context> contextProvider) {
    return new VibratorHelper_Factory(contextProvider);
  }

  public static VibratorHelper newInstance(Context context) {
    return new VibratorHelper(context);
  }
}
