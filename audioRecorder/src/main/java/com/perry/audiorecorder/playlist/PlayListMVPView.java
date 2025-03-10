package com.perry.audiorecorder.playlist;

import com.perry.audiorecorder.db.RecordingItem;
import com.perry.audiorecorder.mvpbase.IMVPView;
import java.io.IOException;

public interface PlayListMVPView extends IMVPView {
  void notifyListAdapter();

  void setRecordingListVisible();

  void setRecordingListInVisible();

  void setEmptyLabelVisible();

  void setEmptyLabelInVisible();

  void startWatchingForFileChanges();

  void stopWatchingForFileChanges();

  void notifyListItemChange(Integer position);

  void showError(String message);

  void notifyListItemRemove(Integer position);

  void pauseMediaPlayer(int position);

  void stopMediaPlayer(int currentPlayingItem);

  void startMediaPlayer(int position, RecordingItem recordingItem) throws IOException;

  void resumeMediaPlayer(int position);

  void showFileOptionDialog(int position, RecordingItem recordingItem);

  void shareFileDialog(String filePath);

  void showRenameFileDialog(int position);

  void showDeleteFileDialog(int position);

  void updateProgressInListItem(Integer position);

  void updateTimerInListItem(int position);
}
