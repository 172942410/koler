package com.perry.audiorecorder.audiorecording;

import android.content.Context;
import com.perry.audiorecorder.AppConstants;
import com.perry.audiorecorder.R;
import com.perry.audiorecorder.mvpbase.BasePresenter;
import com.perry.audiorecorder.recordingservice.AudioRecorder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.Locale;

public class AudioRecordPresenterImpl<V extends AudioRecordMVPView> extends BasePresenter<V>
    implements AudioRecordPresenter<V> {

  private boolean mIsRecording = false;
  private boolean mIsRecordingPaused = false;

  public AudioRecordPresenterImpl(CompositeDisposable compositeDisposable) {
    super(compositeDisposable);
  }

  @Override
  public void onToggleRecodingStatus() {
    if (!mIsRecording) {
      mIsRecording = true;
      getAttachedView().startServiceAndBind();
      getAttachedView().toggleRecordButton();
      getAttachedView().setPauseButtonVisible();
      getAttachedView().togglePauseStatus();
      getAttachedView().setScreenOnFlag();
    } else {
      stopRecording();
    }
  }

  @Override public void onTogglePauseStatus() {
    getAttachedView().setPauseButtonVisible();
    mIsRecordingPaused = !mIsRecordingPaused;
    if (mIsRecordingPaused) {
      getAttachedView().pauseRecord();
    } else {
      getAttachedView().resumeRecord();
    }
  }

  @Override public boolean isRecording() {
    return mIsRecording;
  }

  @Override public boolean isPaused() {
    return mIsRecordingPaused;
  }

  @Override public void onAttach(V view) {
    super.onAttach(view);
    getAttachedView().bindToService();
  }

  @Override public void onViewInitialised() {
    getAttachedView().updateChronometer(getChronometerText(new AudioRecorder.RecordTime()));
    getAttachedView().toggleRecordButton();
  }

  @Override public void onDetach() {
    getAttachedView().unbindFromService();
    super.onDetach();
  }

  private void stopRecording() {
    mIsRecording = false;
    mIsRecordingPaused = false;
    getAttachedView().stopServiceAndUnBind();
    getAttachedView().toggleRecordButton();
    getAttachedView().clearScreenOnFlag();
    getAttachedView().updateChronometer(getChronometerText(new AudioRecorder.RecordTime()));
    getAttachedView().setPauseButtonInVisible();
    getAttachedView().togglePauseStatus();
  }

  private final Consumer<AudioRecorder.RecordTime> recordTimeConsumer =
      recordTime -> getAttachedView().updateChronometer(getChronometerText(recordTime));

  private String getChronometerText(AudioRecorder.RecordTime recordTime) {
    return String.format(Locale.getDefault(), "%02d:%02d:%02d",
        recordTime.hours,
        recordTime.minutes,
        recordTime.seconds);
  }

  @Override public void onServiceStatusAvailable(boolean isRecoding, boolean isRecordingPaused) {
    mIsRecording = isRecoding;
    mIsRecordingPaused = isRecordingPaused;
    if (mIsRecording) {
      getAttachedView().setPauseButtonVisible();
      getAttachedView().togglePauseStatus();
      getAttachedView().linkGLViewToHandler();
      getAttachedView().toggleRecordButton();
      getCompositeDisposable().add(getAttachedView().subscribeForTimer(recordTimeConsumer));
    } else {
      getAttachedView().unbindFromService();
    }
  }

  @Override public void onServiceUpdateReceived(String actionExtra) {
    switch (actionExtra) {
      case AppConstants.ACTION_PAUSE:
        mIsRecordingPaused = true;
        getAttachedView().togglePauseStatus();
        break;
      case AppConstants.ACTION_RESUME:
        mIsRecordingPaused = false;
        getAttachedView().togglePauseStatus();
        break;
      case AppConstants.ACTION_STOP:
        stopRecording();
        break;
    }
  }
}
