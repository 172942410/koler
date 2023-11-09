package com.perry.lib.data.repository.recents

import com.perry.lib.data.model.RecentAccount
import kotlinx.coroutines.flow.Flow

interface RecentsRepository {
    fun getRecent(recentId: Long? = null): Flow<RecentAccount?>
    fun getRecents(filter: String? = null): Flow<List<RecentAccount>>
}