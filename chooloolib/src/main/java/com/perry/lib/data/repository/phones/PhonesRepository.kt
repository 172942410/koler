package com.perry.lib.data.repository.phones

import com.perry.lib.data.model.PhoneAccount
import kotlinx.coroutines.flow.Flow

interface PhonesRepository {
    fun getPhones(contactId: Long? = null, filter: String? = null): Flow<List<PhoneAccount>>
}