package com.perry.lib.ui.base.menu

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.perry.lib.ui.base.BaseViewState

abstract class BaseMenuViewState : BaseViewState() {
    protected val _title = MutableLiveData<String>()
    protected val _subtitle = MutableLiveData<String>()

    val title = _title as LiveData<String>
    val subtitle = _subtitle as LiveData<String>

    abstract val menuResList: List<Int>


    open fun onMenuItemClick(menuItem: MenuItem) {
        onMenuItemClick(menuItem.itemId)
    }

    open fun onMenuItemClick(itemId: Int) {
    }
}