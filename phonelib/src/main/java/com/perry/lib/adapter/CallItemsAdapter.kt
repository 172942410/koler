package com.perry.lib.adapter

import android.net.Uri
import android.telecom.Call.Details.CAPABILITY_SEPARATE_FROM_CONFERENCE
import com.perry.lib.R
import com.perry.lib.data.model.Call
import com.perry.lib.data.model.ListData
import com.perry.lib.di.module.IoScope
import com.perry.lib.di.module.MainScope
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.interactor.phoneaccounts.PhonesInteractor
import com.perry.lib.ui.widgets.listitemholder.ListItemHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CallItemsAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    @IoScope private val ioScope: CoroutineScope,
    private val phonesInteractor: PhonesInteractor,
    @MainScope private val mainScope: CoroutineScope,
) : ListAdapter<Call>(animationsInteractor) {
    override fun onBindListItem(listItemHolder: ListItemHolder, item: Call, position: Int) {
        listItemHolder.apply {
            ioScope.launch {
                val account = phonesInteractor.lookupAccount(item.number)

                mainScope.launch {
                    account?.photoUri?.let {
                        setImageUri(Uri.parse(it))
                    } ?: run {
                        setImageResource(R.drawable.person)
                    }

                    account?.displayString?.let {
                        titleText = it
                        captionText = item.number
                    } ?: run {
                        titleText = item.number
                    }
                }
            }

            isLeftButtonVisible = true
            isRightButtonVisible = true
            isRightButtonEnabled = true
            isLeftButtonEnabled = item.isCapable(CAPABILITY_SEPARATE_FROM_CONFERENCE)

            setLeftButtonIcon(R.drawable.call_split)
            setRightButtonIcon(R.drawable.call_end)
            setRightButtonIconTint(R.color.negative)
        }
    }

    override fun convertDataToListData(items: List<Call>) = ListData(items)
}