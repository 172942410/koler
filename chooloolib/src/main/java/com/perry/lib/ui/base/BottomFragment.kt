package com.perry.lib.ui.base

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.perry.lib.databinding.BottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

open class BottomFragment(
    private val fragment: BaseFragment<BaseViewState>? = null
) : BottomSheetDialogFragment(), BaseView<BaseViewState> {
    override val viewState: BaseViewState by viewModels()

    private val binding by lazy {
        val contextThemeWrapper = ContextThemeWrapper(activity, activity?.theme)
        BottomDialogBinding.inflate(layoutInflater.cloneInContext(contextThemeWrapper))
    }

    @Inject lateinit var baseActivity: BaseActivity<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showsDialog = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }

    override fun onSetup() {
        fragment?.setOnFinishListener {
            this@BottomFragment.dismiss()
        }

        viewState.apply {
            attach()

            errorEvent.observe(this@BottomFragment) {
                it.ifNew?.let(this@BottomFragment::showError)
            }

            messageEvent.observe(this@BottomFragment) {
                it.ifNew?.let(this@BottomFragment::showMessage)
            }
        }

        childFragmentManager.commit {
            fragment?.let { replace(binding.bottomDialogFragmentPlaceholder.id, it) }
        }
    }

    override fun showError(@StringRes stringResId: Int) {
        baseActivity.viewState.onError(stringResId)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.viewState.onMessage(stringResId)
    }
}