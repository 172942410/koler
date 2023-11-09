package com.perry.audiorecorder.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import com.perry.audiorecorder.activities.PlayListActivity;
import com.perry.audiorecorder.di.qualifiers.ActivityContext;
import com.perry.audiorecorder.di.scopes.ActivityScope;

/**
 * Created by arjun on 12/1/17.
 */

@Module
class PlayListActivityModule {
  @Provides
  @ActivityContext
  @ActivityScope
  Context provideActivityContext(PlayListActivity activity) {
    return activity;
  }
}
