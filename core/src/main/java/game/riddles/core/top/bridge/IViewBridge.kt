package game.riddles.core.top.bridge

import android.content.Context
import game.riddles.core.top.ILayoutLifecycleObserver

interface IViewBridge {
    fun addLayoutLifecycleObserver(observer: ILayoutLifecycleObserver) {}
    fun removeLayoutLifecycleObserver(observer: ILayoutLifecycleObserver) {}
    fun getBridgeContext(): Context? {
        return null
    }
}