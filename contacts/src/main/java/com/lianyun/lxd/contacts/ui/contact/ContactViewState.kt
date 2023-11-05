package com.lianyun.lxd.contacts.ui.contact

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.perry.lib.data.model.ContactAccount
import com.perry.lib.data.model.PhoneAccount
import com.perry.lib.interactor.contacts.ContactsInteractor
import com.perry.lib.interactor.navigation.NavigationsInteractor
import com.perry.lib.interactor.phoneaccounts.PhonesInteractor
import com.perry.lib.ui.base.BaseViewState
import com.perry.lib.util.MutableDataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewState @Inject constructor(
    private val phones: PhonesInteractor,
    private val contacts: ContactsInteractor,
    private val navigations: NavigationsInteractor
) : BaseViewState() {
    val callEvent = MutableDataLiveEvent<String>()
    val contactId = MutableLiveData<Long>()
    val contactImage = MutableLiveData<Uri>()
    var isFavorite = MutableLiveData<Boolean>()
    val contactName = MutableLiveData<String>()
    val showHistoryEvent = MutableDataLiveEvent<String>()

    private var contact: ContactAccount? = null


    private fun withFirstNumber(callback: (PhoneAccount) -> Unit) {
        contact?.let {
            viewModelScope.launch {
                phones.getContactAccounts(it.id).getOrNull(0)?.let(callback::invoke)
            }
        }
    }

    fun onContactId(contactId: Long) {
        viewModelScope.launch {
            contacts.getContact(contactId).collect { contact ->
                this@ContactViewState.contact = contact
                contactName.value = contact?.name ?: "Unknown"
                isFavorite.value = contact?.starred == true
                contact?.photoUri?.let { contactImage.value = Uri.parse(it) }
            }
        }
    }

    fun onSmsClick() {
        withFirstNumber { navigations.sendSMS(it.number) }
    }

    fun onCallClick() {
        withFirstNumber { callEvent.call(it.number) }
    }

    fun onEditClick() {
    }

    fun onHistoryClick() {
        contact?.name?.let { showHistoryEvent.call(it) }
    }

    fun onWhatsappClick() {

    }

    fun onDeleteClick() {
        contact?.let {
            contacts.deleteContact(it.id)
            onFinish()
        }
    }
}