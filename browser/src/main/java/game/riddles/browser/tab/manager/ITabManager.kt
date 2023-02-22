package game.riddles.browser.tab.manager

import android.content.Context
import game.riddles.browser.tab.ITab

internal interface ITabManager {
    fun changeContext(newCtx: Context)
    fun getCurTab(): ITab?
    fun addTab()
    fun changeTab(to: ITab)
    fun removeTab(remove: ITab)
    fun destroy()
    fun setCallback(cb: Callback)
    fun removeCallback(cb: Callback)
    fun listTabs(): List<ITab>

    interface Callback {
        fun onCurTabChanged(curTab: ITab) {}
        fun onTabSetChanged() {}
    }
}