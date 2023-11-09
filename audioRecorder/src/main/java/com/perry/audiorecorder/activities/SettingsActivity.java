package com.perry.audiorecorder.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.perry.audiorecorder.R;
import com.perry.audiorecorder.mvpbase.BaseActivity;
import com.perry.audiorecorder.settings.SettingsFragment;

public class SettingsActivity extends BaseActivity {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preferences);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(R.string.action_settings);
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, new SettingsFragment())
        .commit();
  }
}
