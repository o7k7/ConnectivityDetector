package com.orhunkupeli.connectivitydetector

import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class BaseObservable<LISTENER_CLASS> {
    private val listeners: MutableSet<LISTENER_CLASS> = Collections
        .newSetFromMap(ConcurrentHashMap(1))

    fun registerListener(listener: LISTENER_CLASS) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER_CLASS) {
        listeners.remove(listener)
    }

    protected fun getListeners(): Set<LISTENER_CLASS> {
        return Collections.unmodifiableSet(listeners)
    }
}