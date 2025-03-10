package com.perry.audiorecorder.audiovisualization;


import androidx.annotation.NonNull;

/**
 * Audio visualization view interface
 */
public interface AudioVisualization {

  /**
   * Link view to custom implementation of {@link DbmHandler}.
   *
   * @param dbmHandler instance of DbmHandler
   */
  <T> void linkTo(@NonNull DbmHandler<T> dbmHandler);

  /**
   * Resume audio visualization.
   */
  void onResume();

  /**
   * Pause audio visualization.
   */
  void onPause();

  /**
   * Release resources of audio visualization.
   */
  void release();
}
