package com.perry.lib.di.module

import android.app.KeyguardManager
import android.app.UiModeManager
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.perry.lib.data.repository.calls.CallsRepository
import com.perry.lib.data.repository.calls.CallsRepositoryImpl
import com.perry.lib.data.repository.contacts.ContactsRepository
import com.perry.lib.data.repository.contacts.ContactsRepositoryImpl
import com.perry.lib.data.repository.phones.PhonesRepository
import com.perry.lib.data.repository.phones.PhonesRepositoryImpl
import com.perry.lib.data.repository.rawcontacts.RawContactsRepository
import com.perry.lib.data.repository.rawcontacts.RawContactsRepositoryImpl
import com.perry.lib.data.repository.recents.RecentsRepository
import com.perry.lib.data.repository.recents.RecentsRepositoryImpl
import com.perry.lib.di.factory.contentresolver.ContentResolverFactory
import com.perry.lib.di.factory.contentresolver.ContentResolverFactoryImpl
import com.perry.lib.di.factory.fragment.FragmentFactory
import com.perry.lib.di.factory.fragment.FragmentFactoryImpl
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.interactor.animation.AnimationsInteractorImpl
import com.perry.lib.interactor.audio.AudiosInteractor
import com.perry.lib.interactor.audio.AudiosInteractorImpl
import com.perry.lib.interactor.blocked.BlockedInteractor
import com.perry.lib.interactor.blocked.BlockedInteractorImpl
import com.perry.lib.interactor.callaudio.CallAudiosInteractor
import com.perry.lib.interactor.callaudio.CallAudiosInteractorImpl
import com.perry.lib.interactor.calls.CallsInteractor
import com.perry.lib.interactor.calls.CallsInteractorImpl
import com.perry.lib.interactor.color.ColorsInteractor
import com.perry.lib.interactor.color.ColorsInteractorImpl
import com.perry.lib.interactor.contacts.ContactsInteractor
import com.perry.lib.interactor.contacts.ContactsInteractorImpl
import com.perry.lib.interactor.drawable.DrawablesInteractor
import com.perry.lib.interactor.drawable.DrawablesInteractorImpl
import com.perry.lib.interactor.navigation.NavigationsInteractor
import com.perry.lib.interactor.navigation.NavigationsInteractorImpl
import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.interactor.permission.PermissionsInteractorImpl
import com.perry.lib.interactor.phoneaccounts.PhonesInteractor
import com.perry.lib.interactor.phoneaccounts.PhonesInteractorImpl
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractorImpl
import com.perry.lib.interactor.proximity.ProximitiesInteractor
import com.perry.lib.interactor.proximity.ProximitiesInteractorImpl
import com.perry.lib.interactor.rawcontacts.RawContactsInteractor
import com.perry.lib.interactor.rawcontacts.RawContactsInteractorImpl
import com.perry.lib.interactor.recents.RecentsInteractor
import com.perry.lib.interactor.recents.RecentsInteractorImpl
import com.perry.lib.interactor.sim.SimsInteractor
import com.perry.lib.interactor.sim.SimsInteractorImpl
import com.perry.lib.interactor.string.StringsInteractor
import com.perry.lib.interactor.string.StringsInteractorImpl
import com.perry.lib.interactor.telecom.TelecomInteractor
import com.perry.lib.interactor.telecom.TelecomInteractorImpl
import com.perry.lib.interactor.theme.ThemesInteractor
import com.perry.lib.interactor.theme.ThemesInteractorImpl
import com.perry.lib.util.PreferencesManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable

@InstallIn(SingletonComponent::class)
@Module(includes = [ApplicationModule.BindsModule::class])
class ApplicationModule {
    @Provides
    fun provideDisposables(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver
    //region manager

    @Provides
    fun provideUiManager(@ApplicationContext context: Context): UiModeManager =
        context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

    @Provides
    fun providePowerManager(@ApplicationContext context: Context): PowerManager =
        context.getSystemService(Context.POWER_SERVICE) as PowerManager

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    fun provideTelecomManager(@ApplicationContext context: Context): TelecomManager =
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    @Provides
    fun provideKeyguardManager(@ApplicationContext context: Context): KeyguardManager =
        context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    @Provides
    fun provideTelephonyManager(@ApplicationContext context: Context): TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    fun provideInputMethodManager(@ApplicationContext context: Context): InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager =
        PreferencesManager.getInstance(context)

    @Provides
    fun provideSubscriptionManager(@ApplicationContext context: Context): SubscriptionManager =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    //endregion

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        //region repository

        @Binds
        fun bindCallsRepository(callsRepositoryImpl: CallsRepositoryImpl): CallsRepository

        @Binds
        fun bindPhonesRepository(phonesRepositoryImpl: PhonesRepositoryImpl): PhonesRepository

        @Binds
        fun bindRecentsRepository(recentsRepositoryImpl: RecentsRepositoryImpl): RecentsRepository

        @Binds
        fun bindContactsRepository(contactsRepositoryImpl: ContactsRepositoryImpl): ContactsRepository

        @Binds
        fun bindRawContactsRepoistory(rawContactsRepositoryImpl: RawContactsRepositoryImpl): RawContactsRepository

        //endregion

        //region factory

        @Binds
        fun bindFragmentFactory(fragmentFactoryImpl: FragmentFactoryImpl): FragmentFactory

        @Binds
        fun bindContentResolverFactory(contentResolverFactoryImpl: ContentResolverFactoryImpl): ContentResolverFactory

        //endregion

        //region interactor

        @Binds
        fun bindSimsInteractor(simsInteractorImpl: SimsInteractorImpl): SimsInteractor

        @Binds
        fun bindThemesInteractor(themesInteractor: ThemesInteractorImpl): ThemesInteractor

        @Binds
        fun bindCallsInteractor(callsInteractorImpl: CallsInteractorImpl): CallsInteractor

        @Binds
        fun bindColorsInteractor(colorsInteractorImpl: ColorsInteractorImpl): ColorsInteractor

        @Binds
        fun bindAudiosInteractor(audiosInteractorImpl: AudiosInteractorImpl): AudiosInteractor

        @Binds
        fun bindPhonesInteractor(phonesInteractorImpl: PhonesInteractorImpl): PhonesInteractor

        @Binds
        fun bindStringsInteractor(stringsInteractorImpl: StringsInteractorImpl): StringsInteractor

        @Binds
        fun bindBlockedInteractor(blockedInteractorImpl: BlockedInteractorImpl): BlockedInteractor

        @Binds
        fun bindTelecomInteractor(telecomInteractorImpl: TelecomInteractorImpl): TelecomInteractor

        @Binds
        fun bindRecentsInteractor(recentsInteractorImpl: RecentsInteractorImpl): RecentsInteractor

        @Binds
        fun bindContactsInteractor(contactsInteractorImpl: ContactsInteractorImpl): ContactsInteractor

        @Binds
        fun bindDrawablesInteractor(drawablesInteractorImpl: DrawablesInteractorImpl): DrawablesInteractor

        @Binds
        fun bindAnimationsInteractor(animationsInteractorImpl: AnimationsInteractorImpl): AnimationsInteractor

        @Binds
        fun bindCallAudiosInteractor(callAudiosInteractorImpl: CallAudiosInteractorImpl): CallAudiosInteractor

        @Binds
        fun bindRawContactsInteractor(rawContactsInteractorImpl: RawContactsInteractorImpl): RawContactsInteractor

        @Binds
        fun bindProximitiesInteractor(proximitiesInteractorImpl: ProximitiesInteractorImpl): ProximitiesInteractor

        @Binds
        fun bindPermissionsInteractor(permissionsInteractorImpl: PermissionsInteractorImpl): PermissionsInteractor

        @Binds
        fun bindPreferencesInteractor(preferencesInteractorImpl: PreferencesInteractorImpl): PreferencesInteractor

        @Binds
        fun bindNavigationsInteractor(navigationsInteractorImpl: NavigationsInteractorImpl): NavigationsInteractor

        //endregion
    }
}