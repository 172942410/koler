package com.perry.audiorecorder;

import com.orhanobut.hawk.Hawk;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.DaggerApplication;
import com.perry.audiorecorder.di.ApplicationComponent;
import com.perry.audiorecorder.di.DaggerApplicationComponent;

import javax.inject.Inject;

public class AudioRecorderApp extends DaggerApplication {
  private ApplicationComponent applicationComponent;
//  public AudioRecorderApp(){
//
//  }

  @Inject DispatchingAndroidInjector<DaggerApplication> dispatchingAndroidAppInjector;


  @Override public void onCreate() {
    applicationComponent = DaggerApplicationComponent.builder().application(this).build();
    applicationComponent.inject(this);
    super.onCreate();
//    if (LeakCanary.isInAnalyzerProcess(this)) {
//      return;
//    }
//    LeakCanary.install(this);
    Hawk.init(getApplicationContext()).build();

  }

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return (AndroidInjector<? extends DaggerApplication>) applicationComponent;
  }
}
