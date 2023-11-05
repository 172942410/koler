package com.perry.lib.data.repository.contacts

import com.perry.lib.data.model.ContactAccount
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(filter: String? = null): Flow<List<ContactAccount>>
    fun getContact(contactId: Long): Flow<ContactAccount?>
}