package game.riddles.browser.tab.manager

import android.content.Context
import game.riddles.browser.tab.ITab
import game.riddles.browser.tab.Tab
import game.riddles.core.top.ILayoutLifecycleObserver
import game.riddles.core.utils.forEachRemain

internal class TabManager(context: Context) : ITabManager, ILayoutLifecycleObserver {

    private val tabs = mutableListOf<ITab>()
    private var isDestroyed = false
    private var manageCallback = mutableSetOf<ITabManager.Callback>()
    private var topTab: ITab? = null
    private var useContext: Context? = context

    override fun getCurTab(): ITab? {
        if (isDestroyed) return null
        ensureTabs()
        return topTab
    }

    override fun changeContext(newCtx: Context) {
        if (isDestroyed) return
        useContext = newCtx
        tabs.iterator().forEachRemain { it.changeContext(newCtx) }
    }

    private fun ensureTabs() {
        if (tabs.isEmpty()) {
            newTab().apply {
                addTabInternal(this, isTop = true)
            }
            topTab = tabs[0]
        }
    }

    override fun addTab() {
        if (isDestroyed) return
        addTabInternal(newTab())
        manageCallback.iterator().forEachRemain { it.onTabSetChanged() }
    }

    private fun newTab(): ITab {
        val ctx = useContext ?: throw IllegalStateException("Tab is detached from context.")
        return Tab(ctx).apply {
            (this as? ILayoutLifecycleObserver)?.onLayoutCreate(null)
        }
    }

    private fun addTabInternal(add: ITab, isTop: Boolean = false) {
        if (isTop) tabs.add(0, add) else tabs.add(add)
    }

    override fun changeTab(to: ITab) {
        if (isDestroyed) return
        val curTop = topTab ?: return
        if (curTop == to) return
        (curTop as? ILayoutLifecycleObserver)?.onLayoutPause()
        tabs.remove(to)
        addTabInternal(to, isTop = true)
        topTab = to
        manageCallback.iterator().forEachRemain { it.onCurTabChanged(to) }
        (to as? ILayoutLifecycleObserver)?.onLayoutResume()
    }

    override fun removeTab(remove: ITab) {
        if (isDestroyed) return
        if (tabs.contains(remove)) {
            (remove as? ILayoutLifecycleObserver)?.onLayoutDestroy()
            tabs.remove(remove)
            if (tabs.isEmpty()) {
                ensureTabs()
            } else {
                topTab = tabs[0]
            }
            topTab?.let { topTab ->
                manageCallback.iterator().forEachRemain { it.onCurTabChanged(topTab) }
            }
            manageCallback.iterator().forEachRemain { it.onTabSetChanged() }
        }
    }

    override fun onLayoutResume() {
        (topTab as? ILayoutLifecycleObserver)?.onLayoutResume()
    }

    override fun onLayoutPause() {
        (topTab as? ILayoutLifecycleObserver)?.onLayoutPause()
    }

    override fun onLayoutStop() {
        (topTab as? ILayoutLifecycleObserver)?.onLayoutStop()
    }

    override fun destroy() {
        if (isDestroyed) return
        isDestroyed = true
        useContext = null
        tabs.iterator().forEachRemain { (it as? ILayoutLifecycleObserver)?.onLayoutDestroy() }
        tabs.clear()
        topTab = null
        manageCallback.clear()
    }

    override fun setCallback(cb: ITabManager.Callback) {
        if (isDestroyed) return
        manageCallback.add(cb)
    }

    override fun removeCallback(cb: ITabManager.Callback) {
        manageCallback.remove(cb)
    }

    override fun listTabs(): List<ITab> {
        return tabs.toList()
    }
}