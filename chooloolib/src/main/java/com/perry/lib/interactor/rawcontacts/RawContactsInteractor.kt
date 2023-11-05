package com.perry.lib.interactor.rawcontacts

import com.perry.lib.data.model.RawContactAccount
import com.perry.lib.interactor.base.BaseInteractor
import kotlinx.coroutines.flow.Flow

interface RawContactsInteractor : BaseInteractor<RawContactsInteractor.Listener> {
    interface Listener

    fun getRawContacts(contactId: Long): Flow<List<RawContactAccount>>
}