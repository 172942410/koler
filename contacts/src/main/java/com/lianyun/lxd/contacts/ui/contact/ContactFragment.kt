package com.lianyun.lxd.contacts.ui.contact

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.interactor.telecom.TelecomInteractor
import com.perry.lib.ui.base.BaseFragment
import com.lianyun.lxd.contacts.databinding.ContactBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : BaseFragment<ContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: ContactViewState by activityViewModels()

    private val binding by lazy { ContactBinding.inflate(layoutInflater) }

    @Inject
    lateinit var prompts: PromptsInteractor

    @Inject
    lateinit var telecomInteractor: TelecomInteractor


    override fun onSetup() {
        viewState.apply {
            contactId.observe(this@ContactFragment, ::setRawContactsFragment)

            callEvent.observe(this@ContactFragment) {
                it.ifNew?.let(telecomInteractor::callNumber)
            }

            contactName.observe(this@ContactFragment) {
                binding.contactName.text = it
            }

            contactImage.observe(this@ContactFragment) {
                binding.contactImage.setImageURI(it)
            }

            showHistoryEvent.observe(this@ContactFragment) {
                it.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getRecentsFragment(it))
                }
            }
        }

        binding.apply {
            contactButtonSms.setOnClickListener { viewState.onSmsClick() }
            contactButtonEdit.setOnClickListener { viewState.onEditClick() }
            contactButtonDelete.setOnClickListener { viewState.onDeleteClick() }
            contactButtonHistory.setOnClickListener { viewState.onHistoryClick() }
            contactButtonWhatsapp.setOnClickListener { viewState.onWhatsappClick() }
        }

        arguments?.getLong(ARG_CONTACT_ID)?.let { viewState.onContactId(it) }
    }

    private fun setRawContactsFragment(contactId: Long) {
        val accountsFragment = fragmentFactory.getAccountsFragment(contactId)
        childFragmentManager.commit {
            replace(binding.contactPhonesContainer.id, accountsFragment)
        }
    }

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = ContactFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }
}