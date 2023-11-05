package com.perry.lib.interactor.rawcontacts

import com.perry.lib.data.repository.rawcontacts.RawContactsRepository
import com.perry.lib.interactor.base.BaseInteractorImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsInteractorImpl @Inject constructor(
    private val rawContactsRepository: RawContactsRepository
) :
    BaseInteractorImpl<RawContactsInteractor.Listener>(),
    RawContactsInteractor {

    override fun getRawContacts(contactId: Long) = rawContactsRepository.getRawContacts(contactId)
}