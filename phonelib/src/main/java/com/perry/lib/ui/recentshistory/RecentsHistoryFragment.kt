package com.perry.lib.ui.recentshistory

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.perry.lib.ui.recents.RecentsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentsHistoryFragment : RecentsFragment() {
    override val viewState: RecentsHistoryViewState by activityViewModels()

    override fun onSetup() {
        super.onSetup()
        adapter.groupSimilar = false
    }

    companion object {
        fun newInstance(filter: String? = null) = RecentsHistoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}