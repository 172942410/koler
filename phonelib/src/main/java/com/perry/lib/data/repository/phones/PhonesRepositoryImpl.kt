package com.perry.lib.data.repository.phones

import com.perry.lib.data.model.PhoneAccount
import com.perry.lib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhonesRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : PhonesRepository {
    override fun getPhones(contactId: Long?, filter: String?): Flow<List<PhoneAccount>> =
        contentResolverFactory.getPhonesContentResolver(contactId).apply {
            this.filter = filter
        }.getItemsFlow()
}