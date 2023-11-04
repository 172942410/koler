package com.lianyun.lxd.contacts.ui.main

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.data.model.ContactAccount
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
import com.lianyun.lxd.contacts.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewState @Inject constructor(
    private val navigations: NavigationsInteractor
) : BaseViewState() {
    val filter = MutableLiveData<String?>()
    val searchText = MutableLiveData<String?>()
    val isSearching = MutableLiveData(false)
    val searchHintRes = MutableLiveData(R.string.hint_search_contacts)

    val showMenuEvent = MutableLiveEvent()
    val showContactEvent = MutableDataLiveEvent<Uri>()


    fun onSettingsClick() {
        showMenuEvent.call()
    }

    fun onAddContactClick() {
        navigations.addContact("")
    }

    fun onIntent(intent: Intent) {
        if (intent.action == ACTION_VIEW) {
            showContactEvent.call(Uri.parse(intent.data.toString()))
        }
    }

    fun onSearchTextChange(text: String) {
        filter.value = text
    }

    fun onSearchFocusChange(isFocus: Boolean) {
        if (isFocus) {
            isSearching.value = true
        }
    }

    fun onContactClick(contact: ContactAccount) {
    }
}