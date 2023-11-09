package com.perry.lib.ui.phones

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.perry.lib.adapter.PhonesAdapter
import com.perry.lib.interactor.telecom.TelecomInteractor
import com.perry.lib.data.model.PhoneAccount
import com.perry.lib.ui.briefcontact.BriefContactFragment.Companion.ARG_CONTACT_ID
import com.perry.lib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhonesFragment @Inject constructor() : ListFragment<PhoneAccount, PhonesViewState>() {
    override val viewState: PhonesViewState by activityViewModels()

    @Inject override lateinit var adapter: PhonesAdapter
    @Inject lateinit var telecomInteractor: TelecomInteractor


    override fun onSetup() {
        viewState.onContactId(args.getLong(ARG_CONTACT_ID))
        super.onSetup()
        viewState.callEvent.observe(this@PhonesFragment) {
            it.ifNew?.let(telecomInteractor::callNumber)
        }
        binding.itemsScrollView.fastScroller.isVisible = false
    }

    companion object {
        fun newInstance(contactId: Long? = null) = PhonesFragment().apply {
            arguments = Bundle().apply {
                contactId?.let { putLong(ARG_CONTACT_ID, it) }
            }
        }
    }
}