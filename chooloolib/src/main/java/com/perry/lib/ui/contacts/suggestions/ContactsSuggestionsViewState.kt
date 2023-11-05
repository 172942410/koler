package com.perry.lib.ui.contacts.suggestions

import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.data.repository.contacts.ContactsRepository
import com.perry.lib.ui.contacts.ContactsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsSuggestionsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    contactsRepository: ContactsRepository
) : ContactsViewState(permissions,contactsRepository)