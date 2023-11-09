package com.perry.lib.interactor.recents

import com.perry.lib.data.model.RecentAccount
import com.perry.lib.interactor.base.BaseInteractor
import kotlinx.coroutines.flow.Flow

interface RecentsInteractor : BaseInteractor<RecentsInteractor.Listener> {
    interface Listener

    fun deleteRecent(recentId: Long)
    fun deleteAllRecents()
    fun getRecents(): Flow<List<RecentAccount>>
    fun getRecent(recentId: Long): Flow<RecentAccount?>
    fun getCallTypeImage(@RecentAccount.CallType callType: Int): Int
    fun getLastOutgoingCall(): String
}