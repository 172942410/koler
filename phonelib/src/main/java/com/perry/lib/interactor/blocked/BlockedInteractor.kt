package com.perry.lib.interactor.blocked

import com.perry.lib.interactor.base.BaseInteractor

interface BlockedInteractor : BaseInteractor<BlockedInteractor.Listener> {
    interface Listener

    suspend fun blockNumber(number: String)
    suspend fun unblockNumber(number: String)
    suspend fun isNumberBlocked(number: String): Boolean
}