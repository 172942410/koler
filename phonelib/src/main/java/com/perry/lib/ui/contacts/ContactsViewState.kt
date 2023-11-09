package com.perry.lib.ui.contacts

import android.Manifest.permission.READ_CONTACTS
import com.perry.lib.R
import com.perry.lib.data.model.ContactAccount
import com.perry.lib.data.repository.contacts.ContactsRepository
import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.ui.list.ListViewState
import com.perry.lib.util.DataLiveEvent
import com.perry.lib.util.MutableDataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
open class ContactsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    private val contactsRepository: ContactsRepository,
) :
    ListViewState<ContactAccount>(permissions) {

    override val noResultsIconRes = R.drawable.group
    override val noResultsTextRes = R.string.error_no_results_contacts
    override val permissionsImageRes = R.drawable.group
    override val permissionsTextRes = R.string.error_no_permissions_contacts
    override val requiredPermissions = listOf(READ_CONTACTS)

    private val _showContactEvent = MutableDataLiveEvent<Long>()

    val showContactEvent = _showContactEvent as DataLiveEvent<Long>


    override fun onItemClick(item: ContactAccount) {
        super.onItemClick(item)
        _showContactEvent.call(item.id)
    }

    override fun getItemsFlow(filter: String?): Flow<List<ContactAccount>>? =
        contactsRepository.getContacts(filter)
}