package com.perry.lib.ui.accounts

import android.Manifest.permission.READ_CONTACTS
import androidx.lifecycle.MutableLiveData
import com.perry.lib.R
import com.perry.lib.data.model.RawContactAccount
import com.perry.lib.data.repository.rawcontacts.RawContactsRepository
import com.perry.lib.interactor.navigation.NavigationsInteractor
import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.ui.list.ListViewState
import com.perry.lib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AccountsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    private val navigations: NavigationsInteractor,
    private val rawContactsRepository: RawContactsRepository
) :
    ListViewState<RawContactAccount>(permissions) {

    override val noResultsIconRes = R.drawable.call
    override val noResultsTextRes = R.string.error_no_results_phones
    override val requiredPermissions = listOf(READ_CONTACTS)

    val callEvent = DataLiveEvent<String>()
    val contactId = MutableLiveData(0L)


    override fun onItemRightClick(item: RawContactAccount) {
        super.onItemRightClick(item)
        if (item.type == RawContactAccount.RawContactType.WHATSAPP) {
            navigations.openWhatsapp(item.data)
        }
    }

    override fun getItemsFlow(filter: String?): Flow<List<RawContactAccount>>? =
        contactId.value?.let { rawContactsRepository.getRawContacts(it, filter) }

    fun onContactId(contactId: Long) {
        this.contactId.value = contactId
    }
}
