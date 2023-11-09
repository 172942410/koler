package com.perry.lib.interactor.telecom

import com.perry.lib.interactor.base.BaseInteractor
import com.perry.lib.data.model.SimAccount

interface TelecomInteractor : BaseInteractor<TelecomInteractor.Listener> {
    interface Listener {}

    fun handleMmi(code: String): Boolean
    fun handleSecretCode(code: String): Boolean
    fun handleSpecialChars(code: String): Boolean
    fun callVoicemail()
    fun callNumber(number: String, simAccount: SimAccount? = null)
}