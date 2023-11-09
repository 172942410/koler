package com.perry.audiorecorder;

import android.app.Application;

import com.orhanobut.hawk.Hawk;


public class AudioRecorderApp extends Application {
//  public AudioRecorderApp(){
//
//  }


  @Override public void onCreate() {
    super.onCreate();
//    if (LeakCanary.isInAnalyzerProcess(this)) {
//      return;
//    }
//    LeakCanary.install(this);
    Hawk.init(getApplicationContext()).build();

  }

}
