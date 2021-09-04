package com.chooloo.www.koler.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.databinding.ItemsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.reddit.indicatorfastscroll.FastScrollItemIndicator

abstract class ListFragment<ItemType, Adapter : ListAdapter<ItemType>> :
    BaseFragment(),
    ListContract.View<ItemType> {

    abstract val presenter: ListPresenter<ItemType, out ListFragment<ItemType, Adapter>>

    private val _binding by lazy { ItemsBinding.inflate(layoutInflater) }
    private val _isSearchable by lazy { args.getBoolean(ARG_IS_SEARCHABLE) }
    private val _isHideNoResults by lazy { args.getBoolean(ARG_IS_HIDE_NO_RESULTS, false) }

    override val itemCount get() = adapter.itemCount
    override val searchHint by lazy { getString(R.string.hint_search_items) }

    override var emptyStateText: String?
        get() = _binding.itemsEmptyText.text.toString()
        set(value) {
            _binding.itemsEmptyText.text = value
        }

    abstract val adapter: Adapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        adapter.apply {
            setOnItemClickListener(presenter::onItemClick)
            setOnItemLongClickListener(presenter::onItemLongClick)
        }

        _binding.apply {
            itemsSearchBar.apply {
                hint = searchHint
                visibility = if (_isSearchable) VISIBLE else GONE
                setOnTextChangedListener(presenter::onSearchTextChanged)
            }
            itemsRecyclerView.apply {
                addVeiledItems(10)
                setAdapter(this@ListFragment.adapter.apply {
                    isCompact = args.getBoolean(ARG_IS_COMPACT)
                    setOnSelectingChangeListener { presenter.onIsSelectingChanged(it) }
                })
                veil()
            }
            itemsDeleteButton.setOnClickListener {
                presenter.onDeleteItems((itemsRecyclerView.getRecyclerView().adapter as ListAdapter<*>).selectedItems as ArrayList<ItemType>)
            }
        }
        args.getString(ARG_FILTER)?.let { presenter.applyFilter(it) }
    }


    override fun animateListView() {
        boundComponent.animationInteractor.animateRecyclerView(_binding.itemsRecyclerView.getRecyclerView())
    }

    override fun requestSearchFocus() {
        _binding.itemsSearchBar.requestFocus()
    }

    override fun showEmptyPage(isShow: Boolean) {
        _binding.apply {
            itemsEmptyText.visibility = if (isShow && !_isHideNoResults) VISIBLE else GONE
            itemsRecyclerView.visibility = if (isShow && !_isHideNoResults) GONE else VISIBLE
        }
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
//            _binding.itemsRecyclerView.veil()
        } else {
//            _binding.itemsRecyclerView.unVeil()
        }
    }

    override fun showSelecting(isSelecting: Boolean) {
        _binding.itemsDeleteButton.apply {
            if (isSelecting) {
                boundComponent.animationInteractor.animateIn(this, true)
            } else {
                boundComponent.animationInteractor.showView(this, false)
            }
        }
    }

    override fun setupScrollIndicator() {
        _binding.apply {
            itemsFastScroller.setupWithRecyclerView(
                itemsRecyclerView.getRecyclerView(),
                { position ->
                    adapter.getHeader(position)?.let { FastScrollItemIndicator.Text(it) }
                })
            itemsFastScrollerThumb.setupWithFastScroller(itemsFastScroller)
        }
    }


    companion object {
        const val ARG_FILTER = "filter"
        const val ARG_IS_COMPACT = "is_compact"
        const val ARG_IS_SEARCHABLE = "is_searchable"
        const val ARG_IS_HIDE_NO_RESULTS = "is_hide_no_results"
    }
}