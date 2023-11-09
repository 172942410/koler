package com.perry.lib.di.factory.contentresolver

import com.perry.lib.data.contentresolver.*

interface ContentResolverFactory {
    fun getPhonesContentResolver(contactId: Long? = null): PhonesContentResolver
    fun getRecentsContentResolver(recentId: Long? = null): RecentsContentResolver
    fun getPhoneLookupContentResolver(number: String?): PhoneLookupContentResolver
    fun getRawContactsContentResolver(contactId: Long): RawContactsContentResolver
    fun getContactsContentResolver(contactId: Long? = null): ContactsContentResolver
}