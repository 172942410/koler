package com.perry.audiorecorder.di;

import dagger.Module;
import dagger.Provides;
import com.perry.audiorecorder.di.scopes.FragmentScope;
import com.perry.audiorecorder.playlist.PlayListMVPView;
import com.perry.audiorecorder.playlist.PlayListPresenter;
import com.perry.audiorecorder.playlist.PlayListPresenterImpl;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by arjun on 12/1/17.
 */

@Module
class PlayListFragmentModule {

  @Provides
  @FragmentScope
  PlayListPresenter<PlayListMVPView> providePlayListPresenter(PlayListPresenterImpl<PlayListMVPView> playListPresenter) {
    return playListPresenter;
  }

  @Provides
  @FragmentScope
  CompositeDisposable provideCompositeDisposable() {
    return new CompositeDisposable();
  }
}
