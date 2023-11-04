package com.lianyun.lxd.contacts.di.factory.fragment

import com.lianyun.lxd.contacts.ui.contact.ContactFragment

interface FragmentFactory {
    fun getContactFragment(contactId: Long): ContactFragment
}