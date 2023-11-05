package com.perry.lib.interactor.phoneaccounts

import com.perry.lib.data.model.PhoneAccount
import com.perry.lib.data.model.PhoneLookupAccount
import com.perry.lib.interactor.base.BaseInteractor

interface PhonesInteractor : BaseInteractor<PhonesInteractor.Listener> {
    interface Listener

    suspend fun getContactAccounts(contactId: Long): List<PhoneAccount>
    suspend fun lookupAccount(number: String?): PhoneLookupAccount?
}