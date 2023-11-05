package com.perry.lib.ui.callitems

import com.perry.lib.data.model.Call
import com.perry.lib.data.repository.calls.CallsRepository
import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.ui.list.ListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CallItemsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    private val callsRepository: CallsRepository
) :
    ListViewState<Call>(permissions) {

    override fun onItemLeftClick(item: Call) {
        super.onItemLeftClick(item)
        item.leaveConference()
        onFinish()
    }

    override fun onItemRightClick(item: Call) {
        super.onItemRightClick(item)
        item.reject()
        onFinish()
    }

    override fun getItemsFlow(filter: String?): Flow<List<Call>>? =
        callsRepository.getCalls()
}