package com.chooloo.www.koler.adapter

import android.graphics.Color
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.RecentAccount
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.list.ListData
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.util.getHoursString

class RecentsAdapter(activityComponent: ActivityComponent) :
    ListAdapter<RecentAccount>(activityComponent) {

    override fun onBindListItem(listItem: ListItem, item: RecentAccount) {
        listItem.apply {
            isCompact = component.preferences.isCompact
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            component.phones.lookupAccount(item.number) {
                titleText = it?.name ?: item.number
                it?.let {
                    captionText = "$captionText · ${
                        component.strings.getString(Phone.getTypeLabelResource(it.type))
                    }"
                }
            }

            setImageBackgroundColor(Color.TRANSPARENT)
            setImageResource(component.recents.getCallTypeImage(item.type))
            setImageTint(
                when (item.type) {
                    RecentAccount.TYPE_INCOMING,
                    RecentAccount.TYPE_OUTGOING -> component.colors.getColor(R.color.green_foreground)
                    RecentAccount.TYPE_MISSED,
                    RecentAccount.TYPE_REJECTED,
                    RecentAccount.TYPE_BLOCKED -> component.colors.getColor(R.color.red_foreground)
                    RecentAccount.TYPE_VOICEMAIL -> component.colors.getAttrColor(R.attr.colorOnSecondary)
                    else -> component.colors.getColor(R.color.green_foreground)
                }
            )
        }
    }

    override fun convertDataToListData(data: List<RecentAccount>) = ListData.fromRecents(data)
}