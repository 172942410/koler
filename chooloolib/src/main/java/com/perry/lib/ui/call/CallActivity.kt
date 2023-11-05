package com.perry.lib.ui.call

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.perry.lib.R
import com.perry.lib.databinding.CallBinding
import com.perry.lib.di.factory.fragment.FragmentFactory
import com.perry.lib.interactor.animation.AnimationsInteractor
import com.perry.lib.interactor.dialog.DialogsInteractor
import com.perry.lib.interactor.prompt.PromptsInteractor
import com.perry.lib.interactor.screen.ScreensInteractor
import com.perry.lib.ui.base.BaseActivity
import com.perry.lib.ui.call.CallViewState.UIState
import com.perry.lib.ui.dialpad.DialpadViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity<CallViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: CallViewState by viewModels()

    private val dialpadViewState: DialpadViewState by viewModels()
    private val binding by lazy { CallBinding.inflate(layoutInflater) }

    @Inject lateinit var screens: ScreensInteractor
    @Inject lateinit var dialogs: DialogsInteractor
    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var animations: AnimationsInteractor


    override fun onSetup() {
        screens.showWhenLocked()

        viewState.apply {
            imageRes.observe(this@CallActivity, binding.callImage::setImageResource)
            stateTextColor.observe(this@CallActivity, binding.callStateText::setTextColor)

            name.observe(this@CallActivity) {
                binding.callNameText.text = it
            }

            imageURI.observe(this@CallActivity) {
                binding.callImage.setImageURI(it)
                binding.callImage.isVisible = it != null
            }

            elapsedTime.observe(this@CallActivity) {
                it?.let {
                    animations.show(binding.callTimeText, true)
                    binding.callTimeText.text = DateUtils.formatElapsedTime(it / 1000)
                } ?: run {
                    animations.hide(binding.callTimeText, ifVisible = true, goneOrInvisible = false)
                }
            }

            bannerText.observe(this@CallActivity) {
                it?.let {
                    binding.callBanner.text = it
                    if (binding.callBanner.visibility != View.VISIBLE) {
                        binding.callBanner.visibility = View.VISIBLE
                        animations.show(binding.callBanner, true)
                        animations.focus(binding.callBanner)
                    }
                } ?: run {
                    animations.hide(
                        binding.callBanner,
                        ifVisible = true,
                        goneOrInvisible = false
                    )
                }
            }

            stateText.observe(this@CallActivity) {
                val old = binding.callStateText.text.toString()
                binding.callStateText.text = it
                if (old != it) {
                    animations.focus(binding.callStateText)
                }
            }

            uiState.observe(this@CallActivity) {
                when (it) {
                    UIState.MULTI -> {
                        showActiveLayout()
                        binding.callActions.showMultiCallUI()
                    }

                    UIState.ACTIVE -> {
                        showActiveLayout()
                        binding.callActions.showSingleCallUI()
                    }

                    UIState.INCOMING -> {
                        transitionLayoutTo(R.id.constraint_set_incoming_call)
                    }

                    else -> {
                    }
                }
            }

            isHoldEnabled.observe(this@CallActivity) {
                binding.callActions.isHoldEnabled = it
            }

            isMuteEnabled.observe(this@CallActivity) {
                binding.callActions.isMuteEnabled = it
            }

            isSwapEnabled.observe(this@CallActivity) {
                binding.callActions.isSwapEnabled = it
            }

            isMergeEnabled.observe(this@CallActivity) {
                binding.callActions.isMergeEnabled = it
            }

            isManageEnabled.observe(this@CallActivity) {
                binding.callManageButton.isVisible = it
            }

            isSpeakerEnabled.observe(this@CallActivity) {
                binding.callActions.isSpeakerEnabled = it
            }

            isMuteActivated.observe(this@CallActivity) {
                binding.callActions.isMuteActivated = it
            }

            isHoldActivated.observe(this@CallActivity) {
                binding.callActions.isHoldActivated = it
            }

            isSpeakerActivated.observe(this@CallActivity) {
                binding.callActions.isSpeakerActivated = it
            }

            isBluetoothActivated.observe(this@CallActivity) {
                binding.callActions.isBluetoothActivated = it
            }

            askForRouteEvent.observe(this@CallActivity) {
                it.ifNew?.let {
                    dialogs.askForRoute {
                        viewState.onAudioRoutePicked(it)
                        true
                    }
                }
            }

            showDialerEvent.observe(this@CallActivity) {
                it.ifNew?.let { prompts.showFragment(fragmentFactory.getDialerFragment()) }
            }

            showDialpadEvent.observe(this@CallActivity) {
                it.ifNew?.let { prompts.showFragment(fragmentFactory.getDialpadFragment()) }
            }

            showCallManagerEvent.observe(this@CallActivity) {
                it.ifNew?.let { prompts.showFragment(fragmentFactory.getCallItemsFragment()) }
            }

            selectPhoneHandleEvent.observe(this@CallActivity) {
                it.ifNew?.let {
                    dialogs.askForPhoneAccountHandle(it) {
                        viewState.onPhoneAccountHandleSelected(it)
                        true
                    }
                }
            }

            selectPhoneSuggestionEvent.observe(this@CallActivity) {
                it.ifNew?.let {
                    dialogs.askForPhoneAccountSuggestion(it) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            viewState.onPhoneAccountHandleSelected(it.phoneAccountHandle)
                            return@askForPhoneAccountSuggestion true
                        }
                        false
                    }
                }
            }
        }


        binding.apply {
            callActions.setCallActionsListener(viewState)

            callAnswerButton.setOnClickListener {
                viewState.onAnswerClick()
            }

            callRejectButton.setOnClickListener {
                viewState.onRejectClick()
            }

            callManageButton.setOnClickListener {
                viewState.onManageClick()
            }
        }

        dialpadViewState.char.observe(this@CallActivity, viewState::onCharKey)
    }

    private fun showActiveLayout() {
        transitionLayoutTo(R.id.constraint_set_active_call)
        if (binding.callActions.visibility != View.VISIBLE) {
            animations.show(binding.callActions, true)
        }
    }

    private fun transitionLayoutTo(constraintRes: Int) {
        if (binding.root.currentState != constraintRes) {
            binding.root.setTransition(binding.root.currentState, constraintRes)
            binding.root.transitionToEnd()
        }
    }
}
