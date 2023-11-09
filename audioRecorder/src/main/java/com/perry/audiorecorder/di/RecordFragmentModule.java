package com.perry.audiorecorder.di;

import dagger.Module;
import dagger.Provides;
import com.perry.audiorecorder.audiorecording.AudioRecordMVPView;
import com.perry.audiorecorder.audiorecording.AudioRecordPresenter;
import com.perry.audiorecorder.audiorecording.AudioRecordPresenterImpl;
import com.perry.audiorecorder.di.scopes.FragmentScope;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by arjun on 12/1/17.
 */

@Module
class RecordFragmentModule {

  @Provides
  @FragmentScope
  AudioRecordPresenter<AudioRecordMVPView> provideAudioRecordPresenter(
      AudioRecordPresenterImpl<AudioRecordMVPView> audioRecordPresenter) {
    return audioRecordPresenter;
  }

  @Provides
  @FragmentScope
  CompositeDisposable provideCompositeDisposable() {
    return new CompositeDisposable();
  }
}
