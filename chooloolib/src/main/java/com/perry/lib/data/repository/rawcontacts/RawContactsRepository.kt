package com.perry.lib.data.repository.rawcontacts

import com.perry.lib.data.model.RawContactAccount
import kotlinx.coroutines.flow.Flow

interface RawContactsRepository {
    fun getRawContacts(contactId: Long, filter: String? = null): Flow<List<RawContactAccount>>
}