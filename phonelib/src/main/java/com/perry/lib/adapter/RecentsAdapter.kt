package com.perry.lib.adapter

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.perry.lib.R
import com.perry.lib.data.model.ListData
import com.perry.lib.data.model.RecentAccount
import com.perry.lib.di.module.IoScope
import com.perry.lib.di.module.MainScope
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.interactor.drawable.DrawablesInteractor
import com.perry.lib.interactor.phoneaccounts.PhonesInteractor
import com.perry.lib.interactor.recents.RecentsInteractor
import com.perry.lib.ui.widgets.listitemholder.ListItemHolder
import com.perry.lib.util.getHoursString
import com.perry.lib.util.initials
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val phones: PhonesInteractor,
    private val recents: RecentsInteractor,
    private val drawables: DrawablesInteractor,
    @IoScope private val ioScope: CoroutineScope,
    @MainScope private val mainScope: CoroutineScope,
    @ApplicationContext private val context: Context
) : ListAdapter<RecentAccount>(animations) {

    private var _groupSimilar: Boolean = false

    var groupSimilar: Boolean
        get() = _groupSimilar
        set(value) {
            _groupSimilar = value
        }


    override fun onBindListItem(
        listItemHolder: ListItemHolder,
        item: RecentAccount,
        position: Int
    ) {
        listItemHolder.apply {
            val date = context.getHoursString(item.date)

            captionText = if (item.groupCount > 1) "(${item.groupCount}) $date ·" else "$date ·"

            ioScope.launch {
                val account = phones.lookupAccount(item.number)

                mainScope.launch {
                    titleText = account?.name ?: item.cachedName ?: item.number
                    setImageUri(account?.photoUri?.let(Uri::parse))
                    account?.let {
                        captionText =
                            "$captionText ${
                                Phone.getTypeLabel(
                                    context.resources,
                                    it.type,
                                    it.label
                                )
                            } ·"
                        imageInitials = it.name?.initials() ?: it.number?.getOrNull(0).toString()
                        if (it.name.isNullOrEmpty()) {
                            drawables.getDrawable(R.drawable.person)?.let(::setImageDrawable)
                        }
                    }
                }
            }

            setCaptionImageRes(recents.getCallTypeImage(item.type))
        }
    }

    override fun convertDataToListData(items: List<RecentAccount>) =
        ListData.fromRecents(items, groupSimilar)
}