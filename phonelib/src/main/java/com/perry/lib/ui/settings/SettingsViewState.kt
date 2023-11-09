package com.perry.lib.ui.settings

import com.perry.lib.R
import com.perry.lib.interactor.color.ColorsInteractor
import com.perry.lib.interactor.navigation.NavigationsInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.string.StringsInteractor
import com.perry.lib.interactor.theme.ThemesInteractor
import com.perry.lib.interactor.theme.ThemesInteractor.ThemeMode
import com.perry.lib.ui.base.menu.BaseMenuViewState
import com.perry.lib.util.DataLiveEvent
import com.perry.lib.util.LiveEvent
import com.perry.lib.util.MutableDataLiveEvent
import com.perry.lib.util.MutableLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SettingsViewState @Inject constructor(
    protected val themes: ThemesInteractor,
    protected val colors: ColorsInteractor,
    protected val strings: StringsInteractor,
    protected val navigations: NavigationsInteractor,
    protected val preferences: PreferencesInteractor
) :
    BaseMenuViewState() {
    override val menuResList = listOf(R.menu.menu_chooloo)

    private val _askForThemeModeEvent = MutableLiveEvent()
    private val _askForAnimationsEvent = MutableDataLiveEvent<Boolean>()
    private val _askForColorEvent = MutableDataLiveEvent<Int>()

    val askForThemeModeEvent = _askForThemeModeEvent as LiveEvent
    val askForColorEvent = _askForColorEvent as DataLiveEvent<Int>
    val askForAnimationsEvent = _askForAnimationsEvent as DataLiveEvent<Boolean>

    init {
        _title.value = strings.getString(R.string.settings)
    }

    override fun onMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_chooloo_rate -> navigations.rateApp()
            R.id.menu_chooloo_email -> navigations.sendEmail()
            R.id.menu_chooloo_report_bugs -> navigations.reportBug()
            R.id.menu_chooloo_theme_mode -> _askForThemeModeEvent.call()
            R.id.menu_chooloo_animations -> _askForAnimationsEvent.call(preferences.isAnimations)
            else -> super.onMenuItemClick(itemId)
        }
    }

    fun onColorResponse(color: Int) {
//        preferences.accentTheme = when (color) {
//            colors.getColor(R.color.red_primary) -> RED
//            colors.getColor(R.color.primary) -> BLUE
//            colors.getColor(R.color.green_primary) -> GREEN
//            colors.getColor(R.color.purple_primary) -> PURPLE
//            else -> DEFAULT
//        }
        navigations.goToLauncherActivity()
    }

    fun onAnimationsResponse(response: Boolean) {
        preferences.isAnimations = response
    }

    fun onThemeModeResponse(response: ThemeMode) {
        preferences.themeMode = response
        themes.applyThemeMode(response)
        navigations.goToLauncherActivity()
    }
}