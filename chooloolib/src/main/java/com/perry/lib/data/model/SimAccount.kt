package com.perry.lib.data.model

import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import com.perry.lib.util.fullAddress
import com.perry.lib.util.fullLabel

data class SimAccount(
    val index: Int,
    val phoneAccount: PhoneAccount
) {
    val label: String
    val address: String
    val phoneAccountHandle: PhoneAccountHandle get() = phoneAccount.accountHandle

    init {
        label = phoneAccount.fullLabel()
        address = phoneAccount.fullAddress()
    }
}