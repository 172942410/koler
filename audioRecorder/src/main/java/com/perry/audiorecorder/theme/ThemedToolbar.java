package com.perry.audiorecorder.theme;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ThemedToolbar extends Toolbar implements Themed {
  public ThemedToolbar(Context context) {
    this(context, null);
  }

  public ThemedToolbar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ThemedToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void refreshTheme(ThemeHelper themeHelper) {
    setBackgroundColor(themeHelper.getPrimaryColor());
  }
}
