package com.perry.lib.interactor.proximity

import com.perry.lib.interactor.base.BaseInteractor

interface ProximitiesInteractor : BaseInteractor<ProximitiesInteractor.Listener> {
    interface Listener

    fun acquire()
    fun release()
}