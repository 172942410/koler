package com.perry.lib.adapter

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import com.perry.lib.data.model.ListData
import com.perry.lib.databinding.ListItemBinding
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.ui.widgets.listitemholder.ListItemHolder
import com.perry.lib.ui.widgets.listitemholder.MenuItemHolder
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class MenuAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor
) : ListAdapter<MenuItem>(animationsInteractor) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MenuItemHolder(
        ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindListItem(listItemHolder: ListItemHolder, item: MenuItem, position: Int) {
        listItemHolder.apply {
            isClickable = item.isEnabled
            titleText = item.title.toString()
            if (SDK_INT >= VERSION_CODES.O) {
                captionText = item.contentDescription?.toString()
            }

            item.icon?.let(::setImageDrawable)
        }
    }

    override fun convertDataToListData(items: List<MenuItem>) = ListData(items)
}