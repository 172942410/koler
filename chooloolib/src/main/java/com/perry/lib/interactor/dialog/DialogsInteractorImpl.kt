package com.perry.lib.interactor.dialog

import android.content.Context
import android.os.Build.VERSION_CODES.Q
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.perry.lib.R
import com.perry.lib.data.model.SimAccount
import com.perry.lib.di.factory.fragment.FragmentFactory
import com.perry.lib.interactor.callaudio.CallAudiosInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.perry.lib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.interactor.sim.SimsInteractor
import com.perry.lib.interactor.string.StringsInteractor
import com.perry.lib.interactor.theme.ThemesInteractor.ThemeMode
import com.perry.lib.ui.base.BaseActivity
import com.perry.lib.util.baseobservable.BaseObservable
import com.perry.lib.util.fullLabel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class DialogsInteractorImpl @Inject constructor(
    @ActivityContext context: Context,
    private val sims: SimsInteractor,
    private val strings: StringsInteractor,
    private val prompts: PromptsInteractor,
    private val telecomManager: TelecomManager,
    private val callAudios: CallAudiosInteractor,
    private val fragmentsFactory: FragmentFactory,
    private val preferences: PreferencesInteractor,
) : BaseObservable<DialogsInteractor.Listener>(), DialogsInteractor {

    override fun askForBoolean(
        @StringRes titleRes: Int,
        isActivated: Boolean,
        callback: (result: Boolean) -> Boolean
    ) {
        prompts.showFragment(fragmentsFactory.getPromptFragment(
            strings.getString(R.string.prompt_yes_or_no),
            strings.getString(titleRes),
            isActivated
        ).apply {
            setOnItemClickListener(callback::invoke)
        })
    }

    override fun askForValidation(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit) {
        prompts.showFragment(fragmentsFactory.getPromptFragment(
            strings.getString(R.string.prompt_are_you_sure),
            strings.getString(titleRes),
            true
        ).apply {
            setOnItemClickListener {
                callback.invoke(it)
                true
            }
        })
    }


    override fun askForChoice(
        choices: List<String>,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        selectedChoiceIndex: Int?,
        choiceCallback: (String) -> Boolean
    ) {
        prompts.showFragment(
            fragmentsFactory.getChoicesFragment(titleRes, subtitleRes, choices, selectedChoiceIndex)
                .apply { setOnChoiceClickListener(choiceCallback) }
        )
    }

    override fun <T> askForChoice(
        choices: List<T>,
        choiceToString: (T) -> String,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        selectedChoice: T?,
        choiceCallback: (T) -> Boolean
    ) {
        val objectsToStringMap = choices.associateBy({ choiceToString.invoke(it) }, { it })
        askForChoice(
            objectsToStringMap.keys.toList(),
            titleRes,
            subtitleRes,
            choices.indexOf(selectedChoice)
        ) {
            choiceCallback.invoke(objectsToStringMap[it]!!)
        }
    }

    override fun askForColor(
        colorsArrayRes: Int,
        callback: (selectedColor: Int) -> Unit,
        noColorOption: Boolean,
        selectedColor: Int?
    ) {
//        ColorSheet().colorPicker(
//            colors = activity.resources.getIntArray(R.array.accent_colors),
//            listener = callback::invoke,
//            noColorOption = true,
//            selectedColor = selectedColor
//        ).show(activity.supportFragmentManager)
    }

    override fun askForSim(callback: (SimAccount?) -> Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            askForChoice(
                choices = sims.getSimAccounts(),
                choiceCallback = callback::invoke,
                choiceToString = SimAccount::label,
                titleRes = R.string.hint_sim_account,
                subtitleRes = R.string.explain_choose_sim_account
            )
        }
    }

    override fun askForDefaultPage(callback: (Page) -> Boolean) {
        askForChoice(
            choices = Page.values().toList(),
            titleRes = R.string.hint_default_page,
            choiceCallback = callback::invoke,
            subtitleRes = R.string.explain_choose_default_page,
            selectedChoice = preferences.defaultPage,
            choiceToString = { strings.getString(it.titleRes) }
        )
    }

    override fun askForThemeMode(callback: (ThemeMode) -> Boolean) {
        askForChoice(
            choices = ThemeMode.values().toList(),
            titleRes = R.string.hint_theme_mode,
            choiceCallback = callback::invoke,
            subtitleRes = R.string.explain_choose_theme_mode,
            selectedChoice = preferences.themeMode,
            choiceToString = { strings.getString(it.titleRes) }
        )
    }

    override fun askForIncomingCallMode(callback: (IncomingCallMode) -> Boolean) {
        askForChoice(
            choices = IncomingCallMode.values().toList(),
            titleRes = R.string.hint_incoming_call_mode,
            choiceCallback = callback::invoke,
            subtitleRes = R.string.explain_choose_incoming_call_mode,
            selectedChoice = preferences.incomingCallMode,
            choiceToString = { strings.getString(it.titleRes) }
        )
    }

    override fun askForRoute(callback: (CallAudiosInteractor.AudioRoute) -> Boolean) {
        askForChoice(
            choiceCallback = callback::invoke,
            titleRes = R.string.action_choose_audio_route,
            subtitleRes = R.string.explain_choose_audio_route,
            choiceToString = { strings.getString(it.stringRes) },
            selectedChoice = callAudios.audioRoute,
            choices = callAudios.supportedAudioRoutes.toList()
        )
    }

    override fun askForPhoneAccountHandle(
        phonesAccountHandles: List<PhoneAccountHandle>,
        callback: (PhoneAccountHandle) -> Boolean
    ) {
        askForChoice(
            choiceCallback = callback::invoke,
            titleRes = R.string.action_choose_phone_account,
            subtitleRes = R.string.explain_choose_phone_account,
            choiceToString = { telecomManager.getPhoneAccount(it).fullLabel() },
            choices = phonesAccountHandles
        )
    }

    @RequiresApi(Q)
    override fun askForPhoneAccountSuggestion(
        phoneAccountSuggestions: List<PhoneAccountSuggestion>,
        callback: (PhoneAccountSuggestion) -> Boolean
    ) {
        askForChoice(
            choiceCallback = callback::invoke,
            titleRes = R.string.action_choose_phone_account,
            subtitleRes = R.string.explain_choose_phone_account,
            choiceToString = { telecomManager.getPhoneAccount(it.phoneAccountHandle).fullLabel() },
            choices = phoneAccountSuggestions
        )
    }
}
