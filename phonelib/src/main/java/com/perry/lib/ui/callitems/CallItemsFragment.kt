package com.perry.lib.ui.callitems

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.perry.lib.adapter.CallItemsAdapter
import com.perry.lib.data.model.Call
import com.perry.lib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallItemsFragment @Inject constructor() : ListFragment<Call, CallItemsViewState>() {
    override val viewState: CallItemsViewState by viewModels()

    @Inject override lateinit var adapter: CallItemsAdapter


    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = false
            empty.emptyText.isVisible = false
            itemsScrollView.isVisible = !isShow
        }
    }
}