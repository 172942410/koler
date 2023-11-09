package com.perry.lib.adapter

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.perry.lib.R
import com.perry.lib.data.model.ListData
import com.perry.lib.data.model.PhoneAccount
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.ui.widgets.listitemholder.ListItemHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PhonesAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    @ApplicationContext private val context: Context
) : ListAdapter<PhoneAccount>(animationsInteractor) {
    override fun onBindListItem(listItemHolder: ListItemHolder, item: PhoneAccount, position: Int) {
        listItemHolder.apply {
            isImageVisible = false
            titleText = item.number
            isRightButtonVisible = true
            isRightButtonEnabled = true
            captionText = Phone.getTypeLabel(context.resources, item.type, item.label).toString()

            setRightButtonIcon(R.drawable.call)
        }
    }

    override fun convertDataToListData(items: List<PhoneAccount>) =
        ListData.fromPhones(items, withHeader = false)
}