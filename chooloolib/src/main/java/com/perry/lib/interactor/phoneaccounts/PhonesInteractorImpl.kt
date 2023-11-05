package com.perry.lib.interactor.phoneaccounts

import com.perry.lib.data.model.PhoneAccount
import com.perry.lib.data.model.PhoneLookupAccount
import com.perry.lib.di.factory.contentresolver.ContentResolverFactory
import com.perry.lib.interactor.base.BaseInteractorImpl
import io.reactivex.exceptions.OnErrorNotImplementedException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesInteractorImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory,
) : BaseInteractorImpl<PhonesInteractor.Listener>(), PhonesInteractor {

    override suspend fun lookupAccount(number: String?): PhoneLookupAccount? =
        if (number == null || number.isEmpty()) {
            PhoneLookupAccount.PRIVATE
        } else try {
            contentResolverFactory.getPhoneLookupContentResolver(number).queryItems().getOrNull(0)
        } catch (e: OnErrorNotImplementedException) {
            null
        }

    override suspend fun getContactAccounts(contactId: Long): List<PhoneAccount> =
        contentResolverFactory.getPhonesContentResolver(contactId).queryItems().toList()
}