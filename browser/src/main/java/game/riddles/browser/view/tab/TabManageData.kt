package game.riddles.browser.view.tab

import android.app.Application
import androidx.lifecycle.MutableLiveData
import game.riddles.browser.factory.sTabManager
import game.riddles.browser.tab.ITab
import game.riddles.browser.tab.manager.ITabManager
import game.riddles.core.adapter.BaseItemData
import game.riddles.core.top.BaseViewData

class TabManageData(application: Application) : BaseViewData(application), ITabManager.Callback {
    val tabList = MutableLiveData<List<BaseItemData>>()
    val finishTabManagerActivity = MutableLiveData<Boolean>()

    init {
        initTabItems()
        sTabManager?.setCallback(this)
    }

    private fun initTabItems() {
        val tabManage = sTabManager ?: return
        tabManage.listTabs()
            .map {
                ItemTabManage(
                    it,
                    onRemoveTab = { tab -> removeTab(tab) },
                    onChangeTab = { tab -> changeTab(tab) })
            }
            .apply {
                tabList.value = this
            }
    }

    private fun removeTab(tab: ITab) {
        sTabManager?.removeTab(tab)
    }

    private fun changeTab(tab: ITab) {
        sTabManager?.changeTab(tab)
        finishTabManagerActivity.value = true
    }

    fun addNewTab() {
        sTabManager?.addTab()
    }

    override fun onTabSetChanged() {
        initTabItems()
    }

    override fun onCleared() {
        super.onCleared()
        sTabManager?.removeCallback(this)
    }
}