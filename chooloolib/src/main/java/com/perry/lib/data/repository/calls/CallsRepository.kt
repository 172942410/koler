package com.perry.lib.data.repository.calls

import com.perry.lib.data.model.Call
import kotlinx.coroutines.flow.Flow

interface CallsRepository {
    fun getCalls(): Flow<List<Call>>
}