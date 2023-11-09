package com.perry.lib.interactor.screen

import android.view.MotionEvent
import com.perry.lib.interactor.base.BaseInteractor

interface ScreensInteractor : BaseInteractor<ScreensInteractor.Listener> {
    interface Listener

    fun showWhenLocked()
    fun disableKeyboard()
    fun ignoreEditTextFocus(event: MotionEvent)
}