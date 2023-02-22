package game.riddles.browser.view.tab

import android.view.View
import game.riddles.browser.tab.ITab
import game.riddles.core.adapter.BaseItemData

class ItemTabManage(
    val tab: ITab,
    private val onRemoveTab: (tab: ITab) -> Unit,
    private val onChangeTab: (tab: ITab) -> Unit
) : BaseItemData() {

    fun removeTab(view: View) {
        onRemoveTab(tab)
    }

    fun changeTab(view: View) {
        onChangeTab(tab)
    }
}