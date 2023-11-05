package com.lianyun.lxdphone.ui.settings

import android.Manifest
import com.perry.lib.interactor.color.ColorsInteractor
import com.perry.lib.interactor.navigation.NavigationsInteractor
import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.perry.lib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.perry.lib.interactor.recents.RecentsInteractor
import com.perry.lib.interactor.string.StringsInteractor
import com.perry.lib.interactor.theme.ThemesInteractor
import com.perry.lib.ui.settings.SettingsViewState
import com.perry.lib.util.DataLiveEvent
import com.perry.lib.util.LiveEvent
import com.perry.lib.util.MutableDataLiveEvent
import com.perry.lib.util.MutableLiveEvent
import com.lianyun.lxdphone.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewState @Inject constructor(
    colors: ColorsInteractor,
    themes: ThemesInteractor,
    strings: StringsInteractor,
    navigations: NavigationsInteractor,
    preferences: PreferencesInteractor,
    private val recents: RecentsInteractor,
    private val permissions: PermissionsInteractor
) :
    SettingsViewState(themes, colors, strings, navigations, preferences) {

    override val menuResList = arrayListOf(R.menu.menu_koler) + super.menuResList

    private val _askForDefaultPageEvent = MutableLiveEvent()
    private val _askForIncomingCallModeEvent = MutableLiveEvent()
    private val _askForDialpadTonesEvent = MutableDataLiveEvent<Boolean>()
    private val _askForGroupRecentsEvent = MutableDataLiveEvent<Boolean>()
    private val _clearRecentsEvent = MutableLiveEvent()
    private val _askForDialpadVibrateEvent = MutableDataLiveEvent<Boolean>()

    val askForDefaultPageEvent = _askForDefaultPageEvent as LiveEvent
    val askForIncomingCallModeEvent = _askForIncomingCallModeEvent as LiveEvent
    val askForGroupRecentsEvent = _askForGroupRecentsEvent as DataLiveEvent<Boolean>
    val clearRecentsEvent = _clearRecentsEvent as LiveEvent
    val askForDialpadTonesEvent = _askForDialpadTonesEvent as DataLiveEvent<Boolean>
    val askForDialpadVibrateEvent = _askForDialpadVibrateEvent as DataLiveEvent<Boolean>


    override fun onMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_koler_default_page -> _askForDefaultPageEvent.call()
            R.id.menu_koler_incoming_call_mode -> _askForIncomingCallModeEvent.call()
            R.id.menu_koler_dialpad_tones -> _askForDialpadTonesEvent.call(preferences.isDialpadTones)
            R.id.menu_koler_group_recents -> _askForGroupRecentsEvent.call(preferences.isGroupRecents)
            R.id.menu_koler_clear_recents -> _clearRecentsEvent.call()
            R.id.menu_koler_dialpad_vibrate -> _askForDialpadVibrateEvent.call(preferences.isDialpadVibrate)
            else -> super.onMenuItemClick(itemId)
        }
    }


    fun onDefaultPageResponse(response: Page) {
        preferences.defaultPage = response
    }

    fun onDialpadTones(response: Boolean) {
        preferences.isDialpadTones = response
    }

    fun onDialpadVibrate(response: Boolean) {
        preferences.isDialpadVibrate = response
    }

    fun onGroupRecents(response: Boolean) {
        preferences.isGroupRecents = response
        navigations.goToLauncherActivity()
    }

    fun onClearRecents() {
        permissions.runWithPermissions(arrayOf(Manifest.permission.WRITE_CALL_LOG), {
            recents.deleteAllRecents()
            onFinish()
        }, {
            onError(com.perry.lib.R.string.error_no_permissions_edit_call_log)
        })
    }

    fun onIncomingCallMode(response: IncomingCallMode) {
        preferences.incomingCallMode = response
    }
}
