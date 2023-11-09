package com.perry.lib.ui.recents

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.perry.lib.adapter.RecentsAdapter
import com.perry.lib.data.model.RecentAccount
import com.perry.lib.interactor.preferences.PreferencesInteractor
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.ui.list.ListFragment
import com.perry.lib.ui.recentshistory.RecentsHistoryViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class RecentsFragment @Inject constructor() : ListFragment<RecentAccount, RecentsViewState>() {
    @Inject override lateinit var adapter: RecentsAdapter
    override val viewState: RecentsViewState by activityViewModels()

    private val recentsHistoryViewState: RecentsHistoryViewState by activityViewModels()

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var preferences: PreferencesInteractor


    override fun onSetup() {
        super.onSetup()

        adapter.groupSimilar = args.getBoolean(ARG_IS_GROUPED, preferences.isGroupRecents)

        viewState.showRecentEvent.observe(this@RecentsFragment) { ev ->
            ev.ifNew?.let {
                if (it.groupCount > 1) {
                    prompts.showFragment(
                        fragmentFactory.getRecentsHistoryFragment().apply {
                            arguments = arguments ?: Bundle()
                            requireArguments().putBoolean(ARG_OBSERVE, false)
                        })
                    recentsHistoryViewState.onItemsChanged(it.groupAccounts)
                } else {
                    prompts.showFragment(fragmentFactory.getRecentFragment(it.id))
                }
            }
        }
    }

    companion object {
        private const val ARG_IS_GROUPED = "is_grouped"

        fun newInstance(filter: String? = null, isGrouped: Boolean? = null) =
            RecentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILTER, filter)
                    isGrouped?.let { putBoolean(ARG_IS_GROUPED, it) }
                }
            }
    }
}