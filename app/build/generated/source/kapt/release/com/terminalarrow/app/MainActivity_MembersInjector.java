package com.terminalarrow.app;

import com.terminalarrow.app.ui.theme.ThemeManager;
import com.terminalarrow.app.utils.BiometricHelper;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<BiometricHelper> biometricHelperProvider;

  private final Provider<ThemeManager> themeManagerProvider;

  public MainActivity_MembersInjector(Provider<BiometricHelper> biometricHelperProvider,
      Provider<ThemeManager> themeManagerProvider) {
    this.biometricHelperProvider = biometricHelperProvider;
    this.themeManagerProvider = themeManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<BiometricHelper> biometricHelperProvider,
      Provider<ThemeManager> themeManagerProvider) {
    return new MainActivity_MembersInjector(biometricHelperProvider, themeManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectBiometricHelper(instance, biometricHelperProvider.get());
    injectThemeManager(instance, themeManagerProvider.get());
  }

  @InjectedFieldSignature("com.terminalarrow.app.MainActivity.biometricHelper")
  public static void injectBiometricHelper(MainActivity instance, BiometricHelper biometricHelper) {
    instance.biometricHelper = biometricHelper;
  }

  @InjectedFieldSignature("com.terminalarrow.app.MainActivity.themeManager")
  public static void injectThemeManager(MainActivity instance, ThemeManager themeManager) {
    instance.themeManager = themeManager;
  }
}
