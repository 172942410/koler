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

interface FragmentFactory {
    fun getDialpadFragment(): DialpadFragment
    fun getSettingsFragment(): SettingsFragment
    fun getContactsFragment(): ContactsFragment
    fun getCallItemsFragment(): CallItemsFragment
    fun getRecentMenuFragment(): RecentMenuFragment
    fun getPermissionFragment(): PermissionFragment
    fun getRecentFragment(recentId: Long): RecentFragment
    fun getBriefContactMenuFragment(): BriefContactMenuFragment
    fun getDialerFragment(text: String? = null): DialerFragment
    fun getPhonesFragment(contactId: Long? = null): PhonesFragment
    fun getContactsSuggestionsFragment(): ContactsSuggestionsFragment
    fun getBriefContactFragment(contactId: Long): BriefContactFragment
    fun getAccountsFragment(contactId: Long? = null): AccountsFragment
    fun getPromptFragment(title: String, subtitle: String, isActivated: Boolean): PromptFragment
    fun getRecentsHistoryFragment(filter: String? = null): RecentsHistoryFragment
    fun getRecentsFragment(filter: String? = null, isGrouped: Boolean? = null): RecentsFragment
    fun getChoicesFragment(
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int?,
        choices: List<String>,
        selectedChoiceIndex: Int? = null
    ): BaseChoicesFragment
}