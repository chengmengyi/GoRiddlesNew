package game.riddles.browser.web

import game.riddles.core.top.ILayoutLifecycleObserver
import game.riddles.core.utils.forEachRemain

internal abstract class BaseWebEngine : IWebEngine, ILayoutLifecycleObserver {
    private val webStateChangedListeners = mutableSetOf<OnWebStateChanged>()

    final override fun addWebStateChangedListener(lis: OnWebStateChanged) {
        webStateChangedListeners.add(lis)
    }

    final override fun removeWebStateChangedListener(lis: OnWebStateChanged) {
        webStateChangedListeners.remove(lis)
    }

    protected fun notifyWebStateChanged(action: (lis: OnWebStateChanged) -> Unit) {
        webStateChangedListeners.iterator().forEachRemain { action(it) }
    }
}