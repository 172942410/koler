package com.perry.lib.di.factory.contentresolver

import android.content.ContentResolver
import com.perry.lib.data.contentresolver.*
import com.perry.lib.di.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentResolverFactoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ContentResolverFactory {
    override fun getRecentsContentResolver(recentId: Long?) =
        RecentsContentResolver(recentId, contentResolver, ioDispatcher)

    override fun getPhonesContentResolver(contactId: Long?) =
        PhonesContentResolver(contactId, contentResolver, ioDispatcher)

    override fun getContactsContentResolver(contactId: Long?) =
        ContactsContentResolver(contactId, contentResolver, ioDispatcher)

    override fun getRawContactsContentResolver(contactId: Long) =
        RawContactsContentResolver(contactId, contentResolver, ioDispatcher)

    override fun getPhoneLookupContentResolver(number: String?) =
        PhoneLookupContentResolver(number, contentResolver, ioDispatcher)
}