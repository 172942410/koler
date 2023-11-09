package com.perry.audiorecorder.di;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import com.perry.audiorecorder.db.AppDataBase;
import com.perry.audiorecorder.db.RecordItemDataSource;
import com.perry.audiorecorder.di.qualifiers.ApplicationContext;
import javax.inject.Singleton;

@Module
public class ApplicationModule {

  @Provides
  @ApplicationContext
  @Singleton
  Context provideApplicationContext(Application application) {
    return application.getApplicationContext();
  }

  @Provides
  @Singleton
  RecordItemDataSource provideRecordItemDataSource(@ApplicationContext Context context) {
    return AppDataBase.getInstance(context).getRecordItemDataSource();
  }
}
