package com.perry.lib.ui.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.perry.lib.util.DataLiveEvent
import com.perry.lib.util.LiveEvent
import com.perry.lib.util.MutableDataLiveEvent
import com.perry.lib.util.MutableLiveEvent
import javax.inject.Inject

open class BaseViewState @Inject constructor() : ViewModel() {
    //内部用于弹窗等的引用
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
