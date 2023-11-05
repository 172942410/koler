package com.perry.lib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.Contacts
import androidx.core.database.getStringOrNull
import com.perry.lib.di.module.IoDispatcher
import com.perry.lib.data.model.RawContactAccount
import kotlinx.coroutines.CoroutineDispatcher

class RawContactsContentResolver(
    contactId: Long,
    contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    BaseContentResolver<RawContactAccount>(contentResolver, ioDispatcher) {
    override val uri: Uri = Uri.withAppendedPath(
        Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString()),
        Contacts.Entity.CONTENT_DIRECTORY
    )
    override val filterUri: Uri? = null
    override val selection: String? = null
    override val selectionArgs: Array<String>? = null
    override val sortOrder: String = Contacts.Entity.RAW_CONTACT_ID
    override val projection: Array<String> = arrayOf(
        Contacts.Entity.DATA1,
        Contacts.Entity.MIMETYPE,
        Contacts.Entity.CONTACT_ID,
        Contacts.Entity.RAW_CONTACT_ID
    )

    override fun convertCursorToItem(cursor: Cursor) = RawContactAccount(
        data = cursor.getStringOrNull(cursor.getColumnIndex(Contacts.Entity.DATA1)),
        id = cursor.getLong(cursor.getColumnIndex(Contacts.Entity.RAW_CONTACT_ID)),
        contactId = cursor.getLong(cursor.getColumnIndex(Contacts.Entity.CONTACT_ID)),
        type = RawContactAccount.RawContactType.fromContentType(
            cursor.getString(cursor.getColumnIndex(Contacts.Entity.MIMETYPE))
        )
    )
}