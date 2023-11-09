package com.perry.audiorecorder.recordingservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.hawk.Hawk;
import com.perry.audiorecorder.AppConstants;
import com.perry.audiorecorder.R;
import com.perry.audiorecorder.activities.MainActivity;
import com.perry.audiorecorder.db.AppDataBase;
import com.perry.audiorecorder.db.RecordItemDao_Impl;
import com.perry.audiorecorder.db.RecordItemDataSource;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.Locale;

public class AudioRecordService extends Service {
  private static final String LOG_TAG = "RecordingService";

  public AudioRecorder audioRecorder;
  public AudioRecordingDbmHandler handler;
  private ServiceBinder mIBinder;
  private NotificationManager mNotificationManager;
  private static final int NOTIFY_ID = 100;
  private AudioRecorder.RecordTime lastUpdated;
  private boolean mIsClientBound = false;

  @Override public IBinder onBind(Intent intent) {
    mIsClientBound = true;
    return mIBinder;
  }

  public boolean isRecording() {
    return audioRecorder.isRecording();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    handler = new AudioRecordingDbmHandler();

    RecordItemDataSource recordItemDataSource = AppDataBase.getInstance(this).getRecordItemDataSource();
    AudioSaveHelper audioSaveHelper = new AudioSaveHelper(recordItemDataSource);
    audioRecorder = new AudioRecorder(audioSaveHelper);
    handler.addRecorder(audioRecorder);
    mIBinder = new ServiceBinder();
    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
  }

  public AudioRecordingDbmHandler getHandler() {
    return handler;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent.getAction() != null) {
      switch (intent.getAction()) {
        case AppConstants.ACTION_PAUSE:
          pauseRecord();
          break;
        case AppConstants.ACTION_RESUME:
          resumeRecord();
          break;
        case AppConstants.ACTION_STOP:
          if (!mIsClientBound) {
            stopSelf();
          }
      }
      if (mIsClientBound) {
        intent.putExtra(AppConstants.ACTION_IN_SERVICE, intent.getAction());
        intent.setAction(AppConstants.ACTION_IN_SERVICE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      }
    } else {
      startRecording();
      startForeground(NOTIFY_ID, createNotification(new AudioRecorder.RecordTime()));
    }
    return START_STICKY;
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (isRecording()) {
      stopRecodingAndRelease();
    }
  }

  private void stopRecodingAndRelease() {
    audioRecorder.finishRecord();
    handler.stop();
  }

  private void startRecording() {
    boolean prefHighQuality =
        Hawk.get(getApplicationContext().getString(R.string.pref_high_quality_key), false);
    audioRecorder.startRecord(
        prefHighQuality ? Constants.RECORDER_SAMPLE_RATE_HIGH : Constants.RECORDER_SAMPLE_RATE_LOW);
    handler.startDbmThread();
    audioRecorder.subscribeTimer(this::updateNotification);
  }

  public Disposable subscribeForTimer(Consumer<AudioRecorder.RecordTime> timerConsumer) {
    return audioRecorder.subscribeTimer(timerConsumer);
  }

  private void updateNotification(AudioRecorder.RecordTime recordTime) {
    mNotificationManager.notify(NOTIFY_ID, createNotification(recordTime));
  }

  /**
   * 创建通知通道
   * @param channelId
   * @param channelName
   * @return
   */
  @RequiresApi(Build.VERSION_CODES.O)
  private String createNotificationChannel(String channelId, String channelName){
    NotificationChannel chan = new NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE);
    chan.setLightColor(Color.BLUE);
    chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    service.createNotificationChannel(chan);
    return channelId;
  }

  private Notification createNotification(AudioRecorder.RecordTime recordTime) {
    String channelId = null;
    // 8.0 以上需要特殊处理
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      channelId = createNotificationChannel("perry.li", "ForegroundService");
    } else {
      channelId = "";
    }
    lastUpdated = recordTime;
    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(getApplicationContext(),channelId).setSmallIcon(
            R.drawable.ic_media_record)
            .setContentTitle(getString(R.string.notification_recording))
            .setContentText(
                String.format(Locale.getDefault(), getString(R.string.record_time_format),
                    recordTime.hours,
                    recordTime.minutes,
                    recordTime.seconds))
            .addAction(R.drawable.ic_media_stop, getString(R.string.stop_recording),
                getActionIntent(AppConstants.ACTION_STOP))
            .setOngoing(true);
    if (audioRecorder.isPaused()) {
      mBuilder.addAction(R.drawable.ic_media_record, getString(R.string.resume_recording_button),
          getActionIntent(AppConstants.ACTION_RESUME));
    } else {
      mBuilder.addAction(R.drawable.ic_media_pause, getString(R.string.pause_recording_button),
          getActionIntent(AppConstants.ACTION_PAUSE));
    }
    mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
        new Intent[] {new Intent(getApplicationContext(), MainActivity.class)}, PendingIntent.FLAG_IMMUTABLE));

    return mBuilder.build();
  }

  public void pauseRecord() {
    audioRecorder.pauseRecord();
    updateNotification(lastUpdated);
  }

  public boolean isPaused() {
    return audioRecorder.isPaused();
  }

  public void resumeRecord() {
    audioRecorder.resumeRecord();
  }

  public class ServiceBinder extends Binder {
    public AudioRecordService getService() {
      return AudioRecordService.this;
    }
  }

  private PendingIntent getActionIntent(String action) {
    Intent pauseIntent = new Intent(this, AudioRecordService.class);
    pauseIntent.setAction(action);
    return PendingIntent.getService(this, 100, pauseIntent, PendingIntent.FLAG_IMMUTABLE);
  }

  @Override public boolean onUnbind(Intent intent) {
    mIsClientBound = false;
    return true;
  }

  @Override public void onRebind(Intent intent) {
    mIsClientBound = true;
  }
}
