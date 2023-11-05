package com.perry.lib.interactor.prompt

import com.perry.lib.interactor.base.BaseInteractor
import com.perry.lib.ui.base.BaseFragment

interface PromptsInteractor : BaseInteractor<PromptsInteractor.Listener> {
    interface Listener

    fun showFragment(fragment: BaseFragment<*>, tag: String? = null)
}