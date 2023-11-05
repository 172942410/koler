package com.perry.lib.adapter

import com.perry.lib.R
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.interactor.string.StringsInteractor
import com.perry.lib.data.model.ListData
import com.perry.lib.data.model.RawContactAccount
import com.perry.lib.ui.widgets.listitemholder.ListItemHolder
import javax.inject.Inject

class AccountsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val strings: StringsInteractor
) : ListAdapter<RawContactAccount>(animations) {
    override fun onBindListItem(
        listItemHolder: ListItemHolder,
        item: RawContactAccount,
        position: Int
    ) {
        listItemHolder.apply {
            captionText = null
            isImageVisible = false
            isRightButtonVisible = true
            titleText = strings.getString(item.type.titleStringRes)

            if (item.type == RawContactAccount.RawContactType.WHATSAPP) {
                setRightButtonIcon(R.drawable.whatsapp)
            }
        }
    }

    override fun convertDataToListData(items: List<RawContactAccount>) =
        ListData.fromRawContacts(items, accounts = true, withHeader = false)
}