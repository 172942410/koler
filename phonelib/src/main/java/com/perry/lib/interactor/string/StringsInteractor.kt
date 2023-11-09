package com.perry.lib.interactor.string

import androidx.annotation.StringRes
import com.perry.lib.interactor.base.BaseInteractor

interface StringsInteractor : BaseInteractor<StringsInteractor.Listener> {
    interface Listener

    fun getString(@StringRes stringRes: Int): String
}