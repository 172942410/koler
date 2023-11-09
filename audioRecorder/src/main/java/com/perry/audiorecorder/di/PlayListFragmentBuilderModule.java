package com.perry.audiorecorder.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import com.perry.audiorecorder.di.scopes.FragmentScope;
import com.perry.audiorecorder.playlist.PlayListFragment;

/**
 * Created by arjun on 12/1/17.
 */

@Module
abstract class PlayListFragmentBuilderModule {
  @FragmentScope
  @ContributesAndroidInjector(modules = {PlayListFragmentModule.class})
  abstract PlayListFragment contributePlayListFragment();
}
