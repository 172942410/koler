package com.perry.audiorecorder.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import com.perry.audiorecorder.audiorecording.RecordFragment;
import com.perry.audiorecorder.di.scopes.FragmentScope;

/**
 * Created by arjun on 12/1/17.
 */

@Module
abstract class RecordFragmentBuilderModule {
  @FragmentScope
  @ContributesAndroidInjector(modules = {RecordFragmentModule.class})
  abstract RecordFragment contributeRecordFragment();
}
