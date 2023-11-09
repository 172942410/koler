package com.perry.lib.ui.contacts

import androidx.fragment.app.activityViewModels
import com.perry.lib.adapter.ContactsAdapter
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.data.model.ContactAccount
import com.perry.lib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ContactsFragment @Inject constructor() :
    ListFragment<ContactAccount, ContactsViewState>() {
    override val viewState: ContactsViewState by activityViewModels()

    @Inject lateinit var prompts: PromptsInteractor
    @Inject override lateinit var adapter: ContactsAdapter


    override fun onSetup() {
        super.onSetup()

        viewState.showContactEvent.observe(this@ContactsFragment) { ev ->
            ev.ifNew?.let {
                prompts.showFragment(fragmentFactory.getBriefContactFragment(it))
            }
        }
    }
}