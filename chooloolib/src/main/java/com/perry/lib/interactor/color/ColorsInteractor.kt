package com.perry.lib.interactor.color

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import com.perry.lib.interactor.base.BaseInteractor

interface ColorsInteractor : BaseInteractor<ColorsInteractor.Listener> {
    interface Listener

    fun getColor(@ColorRes colorRes: Int): Int
    fun getAttrColor(@AttrRes colorRes: Int): Int
}