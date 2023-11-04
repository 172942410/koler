package com.lianyun.lxd.contacts.di.factory.fragment

import com.lianyun.lxd.contacts.ui.contact.ContactFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FragmentFactoryImpl @Inject constructor() : FragmentFactory {
    override fun getContactFragment(contactId: Long) = ContactFragment.newInstance(contactId)
}