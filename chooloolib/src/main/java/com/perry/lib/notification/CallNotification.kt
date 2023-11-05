package com.perry.lib.notification

import android.app.Notification
import android.app.Notification.CATEGORY_CALL
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.telecom.Call.Details.CAPABILITY_MUTE
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.perry.lib.R
import com.perry.lib.data.model.Call
import com.perry.lib.data.model.Call.State.DISCONNECTED
import com.perry.lib.data.model.Call.State.DISCONNECTING
import com.perry.lib.di.module.IoScope
import com.perry.lib.interactor.callaudio.CallAudiosInteractor
import com.perry.lib.interactor.calls.CallsInteractor
import com.perry.lib.interactor.color.ColorsInteractor
import com.perry.lib.interactor.phoneaccounts.PhonesInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.perry.lib.interactor.string.StringsInteractor
import com.perry.lib.receiver.CallBroadcastReceiver
import com.perry.lib.receiver.CallBroadcastReceiver.Companion.ACTION_HANGUP
import com.perry.lib.receiver.CallBroadcastReceiver.Companion.ACTION_MUTE
import com.perry.lib.receiver.CallBroadcastReceiver.Companion.ACTION_SPEAKER
import com.perry.lib.receiver.CallBroadcastReceiver.Companion.ACTION_UNMUTE
import com.perry.lib.receiver.CallBroadcastReceiver.Companion.ACTION_UNSPEAKER
import com.perry.lib.ui.call.CallActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class CallNotification @Inject constructor(
    private val calls: CallsInteractor,
    private val colors: ColorsInteractor,
    private val phones: PhonesInteractor,
    private val strings: StringsInteractor,
    @IoScope private val ioScope: CoroutineScope,
    private val callAudios: CallAudiosInteractor,
    private val preferences: PreferencesInteractor,
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat
) : CallsInteractor.Listener, CallAudiosInteractor.Listener {

    private var _call: Call? = null

    private val _channel
        get() = if (_call?.isIncoming == true && preferences.incomingCallMode == IncomingCallMode.POP_UP) {
            _channelImportanceHigh
        } else {
            _channelImportanceLow
        }

    private val _channelImportanceLow by lazy {
        NotificationChannelCompat.Builder(CHANNEL_ID_PRIORITY_LOW, IMPORTANCE_DEFAULT)
            .setName(strings.getString(R.string.call_notification_channel_name))
            .setDescription(strings.getString(R.string.call_notification_channel_description))
            .build()
            .also(notificationManager::createNotificationChannel)
    }

    private val _channelImportanceHigh by lazy {
        NotificationChannelCompat.Builder(CHANNEL_ID_PRIORITY_HIGH, IMPORTANCE_HIGH)
            .setName(strings.getString(R.string.call_notification_channel_name))
            .setDescription(strings.getString(R.string.call_notification_channel_description))
            .build()
            .also(notificationManager::createNotificationChannel)
    }

    private val _contentPendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, CallActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            },
            FLAG_IMMUTABLE
        )
    }

    private val _answerAction by lazy {
        NotificationCompat.Action(
            R.drawable.call,
            strings.getString(R.string.action_answer),
            getCallPendingIntent(ACTION_ANSWER, 0)
        )
    }

    private val _hangupAction by lazy {
        NotificationCompat.Action(
            R.drawable.call_end,
            strings.getString(R.string.action_hangup),
            getCallPendingIntent(ACTION_HANGUP, 1)
        )
    }

    private val _muteAction by lazy {
        NotificationCompat.Action(
            R.drawable.mic,
            strings.getString(R.string.call_action_mute),
            getCallPendingIntent(ACTION_MUTE, 2)
        )
    }

    private val _unmuteAction by lazy {
        NotificationCompat.Action(
            R.drawable.mic_off,
            strings.getString(R.string.call_action_unmute),
            getCallPendingIntent(ACTION_UNMUTE, 3)
        )
    }

    private val _speakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.volume_down,
            strings.getString(R.string.call_action_speaker),
            getCallPendingIntent(ACTION_SPEAKER, 4)
        )
    }

    private val _unspeakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.volume_up,
            strings.getString(R.string.call_action_speaker_off),
            getCallPendingIntent(ACTION_UNSPEAKER, 5)
        )
    }


    override fun onNoCalls() {
        detach()
    }

    override fun onCallChanged(call: Call) {
    }

    override fun onMainCallChanged(call: Call) {
        _call = call
        refresh()
    }

    override fun onMuteChanged(isMuted: Boolean) {
        refresh()
    }

    override fun onAudioRouteChanged(audioRoute: CallAudiosInteractor.AudioRoute) {
        refresh()
    }


    private fun getCallIntent(callAction: String) =
        Intent(context, CallBroadcastReceiver::class.java).apply {
            action = callAction
            putExtra(EXTRA_NOTIFICATION_ID, ID)
        }

    private fun getCallPendingIntent(callAction: String, rc: Int) =
        PendingIntent.getBroadcast(
            context,
            rc,
            getCallIntent(callAction),
            FLAG_CANCEL_CURRENT or FLAG_IMMUTABLE
        )

    private suspend fun buildNotification(call: Call, callback: (Notification) -> Unit) {
        val phoneAccount = phones.lookupAccount(call.number)
        val builder = NotificationCompat.Builder(context, _channel.id)
            .setWhen(0)
            .setOngoing(true)
            .setColorized(true)
            .setPriority(PRIORITY)
            .setOnlyAlertOnce(true)
            .setCategory(CATEGORY_CALL)
            .setContentTitle(phoneAccount?.displayString ?: call.number)
            .setSmallIcon(R.drawable.icon_full_144)
            .setContentIntent(_contentPendingIntent)
            .setColor(colors.getAttrColor(R.attr.colorSecondary))
            .setContentText(strings.getString(call.state.stringRes))

        if (call.state !in arrayOf(DISCONNECTED, DISCONNECTING)) {
            builder.addAction(_hangupAction)
        }

        if (call.isIncoming) {
            builder.addAction(_answerAction)
        } else if (call.state !in arrayOf(DISCONNECTED, DISCONNECTING)) {
            callAudios.isMuted?.let { isMuted ->
                if (call.isCapable(CAPABILITY_MUTE)) {
                    builder.addAction(if (isMuted) _unmuteAction else _muteAction)
                }
            }

            callAudios.isSpeakerOn?.let { isSpeakerOn ->
                builder.addAction(if (isSpeakerOn) _unspeakerAction else _speakerAction)
            }
        }

        callback.invoke(builder.build())
    }


    fun attach() {
        calls.registerListener(this)
        callAudios.registerListener(this)
        Thread.currentThread().setUncaughtExceptionHandler { _, _ -> detach() }
    }

    fun detach() {
        calls.unregisterListener(this)
        callAudios.unregisterListener(this)
        notificationManager.cancel(ID)
    }

    fun show(call: Call) {
        ioScope.launch {
            buildNotification(call) { notificationManager.notify(ID, it) }
        }
    }

    fun refresh() {
        _call?.let(::show)
    }


    companion object {
        const val ID = 420
        const val PRIORITY = NotificationCompat.PRIORITY_HIGH
        const val CHANNEL_ID_PRIORITY_LOW = "cnc_priority_low"
        const val CHANNEL_ID_PRIORITY_HIGH = "cnc_priority_high"
    }
}