package com.perry.lib.ui.recentshistory

import android.content.ClipboardManager
import com.perry.lib.interactor.permission.PermissionsInteractor
import com.perry.lib.data.repository.recents.RecentsRepository
import com.perry.lib.ui.recents.RecentsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentsHistoryViewState @Inject constructor(
    permissions: PermissionsInteractor,
    clipboardManager: ClipboardManager,
    recentsRepository: RecentsRepository
) : RecentsViewState(permissions, clipboardManager, recentsRepository) {
}