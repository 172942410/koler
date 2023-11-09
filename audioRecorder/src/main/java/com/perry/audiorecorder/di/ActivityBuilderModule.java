package com.perry.audiorecorder.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import com.perry.audiorecorder.activities.MainActivity;
import com.perry.audiorecorder.activities.PlayListActivity;
import com.perry.audiorecorder.activities.SettingsActivity;
import com.perry.audiorecorder.di.scopes.ActivityScope;

/**
 * Created by arjun on 12/1/17.
 */

@Module
abstract class ActivityBuilderModule {
  @ActivityScope
  @ContributesAndroidInjector(modules = {MainActivityModule.class, RecordFragmentBuilderModule.class})
  abstract MainActivity contributeMainActivity();

  @ActivityScope
  @ContributesAndroidInjector(modules = {PlayListActivityModule.class, PlayListFragmentBuilderModule.class})
  abstract PlayListActivity contributePlayListActivity();

  @ContributesAndroidInjector()
  abstract SettingsActivity contributeSettingsActivity();
}
