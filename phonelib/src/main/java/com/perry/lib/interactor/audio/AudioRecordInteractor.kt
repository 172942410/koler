package com.perry.lib.interactor.audio

import android.content.Context
import android.media.AudioRecord
import com.perry.lib.interactor.base.BaseInteractor

interface AudioRecordInteractor : BaseInteractor<AudioRecordInteractor.Listener> {
    interface Listener
//    var bufferSize: Int
//    var audioRecord: AudioRecord
//    var buffer: ByteArray

    var isSpeakerOn: Boolean

    fun initRecord()
    fun startRecord()
    fun stopRecord()


    enum class AudioMode(val mode: Int) {
        NORMAL(android.media.AudioManager.MODE_NORMAL),
        IN_CALL(android.media.AudioManager.MODE_IN_CALL),
        CURRENT(android.media.AudioManager.MODE_CURRENT),
        RINGTONE(android.media.AudioManager.MODE_RINGTONE),
        IN_COMMUNICATION(android.media.AudioManager.MODE_IN_COMMUNICATION)
    }

    companion object {
        const val SHORT_VIBRATE_LENGTH: Long = 20
    }
}