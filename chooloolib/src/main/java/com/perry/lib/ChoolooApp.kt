package com.perry.lib

import android.app.Application
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.theme.ThemesInteractor
import javax.inject.Inject

abstract class ChoolooApp : Application() {
    @Inject lateinit var themes: ThemesInteractor
    @Inject lateinit var preferences: PreferencesInteractor

    override fun onCreate() {
        super.onCreate()
        preferences.setDefaultValues()
        themes.applyThemeMode(preferences.themeMode)
    }
}