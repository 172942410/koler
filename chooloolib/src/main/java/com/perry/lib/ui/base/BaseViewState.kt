package com.perry.lib.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.perry.lib.util.DataLiveEvent
import com.perry.lib.util.LiveEvent
import com.perry.lib.util.MutableDataLiveEvent
import com.perry.lib.util.MutableLiveEvent

open class BaseViewState : ViewModel() {
    protected val _finishEvent = MutableLiveEvent()
    protected val _errorEvent = MutableDataLiveEvent<Int>()
    protected val _messageEvent = MutableDataLiveEvent<Int>()

    val finishEvent = _finishEvent as LiveEvent
    val errorEvent = _errorEvent as DataLiveEvent<Int>
    val messageEvent = _messageEvent as DataLiveEvent<Int>


    open fun attach() {}
    open fun detach() {}

    fun onFinish() {
        _finishEvent.call()
    }

    fun onError(@StringRes errMessageRes: Int) {
        _errorEvent.call(errMessageRes)
    }

    fun onMessage(@StringRes messageRes: Int) {
        _messageEvent.call(messageRes)
    }
}
