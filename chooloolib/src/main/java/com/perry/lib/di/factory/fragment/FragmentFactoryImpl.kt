package com.perry.lib.di.factory.fragment

import androidx.annotation.StringRes
import com.perry.lib.ui.accounts.AccountsFragment
import com.perry.lib.ui.base.BaseChoicesFragment
import com.perry.lib.ui.briefcontact.BriefContactFragment
import com.perry.lib.ui.briefcontact.menu.BriefContactMenuFragment
import com.perry.lib.ui.callitems.CallItemsFragment
import com.perry.lib.ui.contacts.ContactsFragment
import com.perry.lib.ui.contacts.suggestions.ContactsSuggestionsFragment
import com.perry.lib.ui.dialer.DialerFragment
import com.perry.lib.ui.dialpad.DialpadFragment
import com.perry.lib.ui.permission.PermissionFragment
import com.perry.lib.ui.phones.PhonesFragment
import com.perry.lib.ui.prompt.PromptFragment
import com.perry.lib.ui.recent.RecentFragment
import com.perry.lib.ui.recent.menu.RecentMenuFragment
import com.perry.lib.ui.recents.RecentsFragment
import com.perry.lib.ui.recentshistory.RecentsHistoryFragment
import com.perry.lib.ui.settings.SettingsFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FragmentFactoryImpl @Inject constructor() : FragmentFactory {
    override fun getDialpadFragment() = DialpadFragment()
    override fun getSettingsFragment() = SettingsFragment()
    override fun getContactsFragment() = ContactsFragment()
    override fun getCallItemsFragment() = CallItemsFragment()
    override fun getPermissionFragment() = PermissionFragment()
    override fun getRecentMenuFragment() = RecentMenuFragment()
    override fun getContactsSuggestionsFragment() = ContactsSuggestionsFragment()
    override fun getDialerFragment(text: String?) = DialerFragment.newInstance(text)
    override fun getRecentFragment(recentId: Long) = RecentFragment.newInstance(recentId)
    override fun getBriefContactMenuFragment() = BriefContactMenuFragment()
    override fun getAccountsFragment(contactId: Long?) =
        AccountsFragment.newInstance(contactId)

    override fun getBriefContactFragment(contactId: Long) =
        BriefContactFragment.newInstance(contactId)

    override fun getPhonesFragment(contactId: Long?) =
        PhonesFragment.newInstance(contactId)

    override fun getPromptFragment(
        title: String,
        subtitle: String,
        isActivated: Boolean
    ): PromptFragment =
        PromptFragment.newInstance(title, subtitle, isActivated)

    override fun getRecentsHistoryFragment(filter: String?) =
        RecentsHistoryFragment.newInstance(filter)

    override fun getRecentsFragment(filter: String?, isGrouped: Boolean?) =
        RecentsFragment.newInstance(filter, isGrouped)

    override fun getChoicesFragment(
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        choices: List<String>,
        selectedChoiceIndex: Int?
    ) = BaseChoicesFragment.newInstance(titleRes, subtitleRes, choices, selectedChoiceIndex)

}