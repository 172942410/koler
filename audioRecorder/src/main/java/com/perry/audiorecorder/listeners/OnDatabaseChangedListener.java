package com.perry.audiorecorder.listeners;

public interface OnDatabaseChangedListener {
  void onNewDatabaseEntryAdded();

  void onDatabaseEntryRenamed();
}