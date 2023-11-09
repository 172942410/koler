package com.perry.lib.data.repository.recents

import com.perry.lib.data.model.RecentAccount
import com.perry.lib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentsRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : RecentsRepository {
    override fun getRecents(filter: String?): Flow<List<RecentAccount>> =
        contentResolverFactory.getRecentsContentResolver().apply {
            this.filter = filter
        }.getItemsFlow()

    override fun getRecent(recentId: Long?) = flow {
        contentResolverFactory.getRecentsContentResolver(recentId).getItemsFlow().collect {
            emit(it.getOrNull(0))
        }
    }
}