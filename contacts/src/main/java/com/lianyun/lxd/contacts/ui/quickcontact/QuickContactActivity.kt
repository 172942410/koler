package com.lianyun.lxd.contacts.ui.quickcontact

import androidx.activity.viewModels
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.ui.base.BaseActivity
import com.lianyun.lxd.contacts.databinding.QuickContactBinding
import com.lianyun.lxd.contacts.di.factory.fragment.FragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuickContactActivity : BaseActivity<QuickContactViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: QuickContactViewState by viewModels()

    private val binding by lazy { QuickContactBinding.inflate(layoutInflater) }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory


    override fun onSetup() {
        viewState.apply {
            showContactEvent.observe(this@QuickContactActivity) {
                it.ifNew?.let { contactId ->
                    prompts.showFragment(fragmentFactory.getContactFragment(contactId).apply {
                        setOnFinishListener { this@QuickContactActivity.viewState.onFragmentFinish() }
                    })
                }
            }
        }

        viewState.onIntent(intent)
    }
}