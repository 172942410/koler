package com.perry.lib.interactor.base

import java.util.*
import kotlin.collections.HashSet


abstract class BaseInteractorImpl<Listener> : BaseInteractor<Listener> {
    private val _listeners by lazy { HashSet<Listener>() }

    private val listeners: Set<Listener>
        get() = Collections.unmodifiableSet(HashSet(_listeners))

    @Synchronized
    override fun registerListener(listener: Listener) {
        _listeners.remove(listener)
        _listeners.add(listener)
        if (_listeners.size == 1) {
            onStartedObserved()
        }
    }

    @Synchronized
    override fun unregisterListener(listener: Listener) {
        _listeners.remove(listener)
        if (_listeners.size == 0) {
            onFinishedObserved()
        }
    }

    override fun invokeListeners(invoker: (Listener) -> Unit) {
        listeners.forEach(invoker::invoke)
    }

    protected open fun onStartedObserved() {}
    protected open fun onFinishedObserved() {}
}
