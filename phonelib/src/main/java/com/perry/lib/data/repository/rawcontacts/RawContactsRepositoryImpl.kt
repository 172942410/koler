package com.perry.lib.data.repository.rawcontacts

import com.perry.lib.data.model.RawContactAccount
import com.perry.lib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : RawContactsRepository {
    override fun getRawContacts(contactId: Long, filter: String?): Flow<List<RawContactAccount>> =
        contentResolverFactory.getRawContactsContentResolver(contactId).apply {
            this.filter = filter
        }.getItemsFlow()
}