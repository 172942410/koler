package com.perry.audiorecorder.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import com.perry.audiorecorder.activities.MainActivity;
import com.perry.audiorecorder.di.qualifiers.ActivityContext;
import com.perry.audiorecorder.di.scopes.ActivityScope;

@Module
public class MainActivityModule {
  @Provides
  @ActivityContext
  @ActivityScope
  Context provideActivityContext(MainActivity activity) {
    return activity;
  }
}
