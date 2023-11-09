package com.perry.lib.data.repository.calls

import com.perry.lib.api.service.CallService
import com.perry.lib.data.model.Call
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallsRepositoryImpl @Inject constructor() : CallsRepository {
    override fun getCalls(): Flow<List<Call>> = flow {
        CallService.sInstance?.calls?.collect(this::emit)
    }
}