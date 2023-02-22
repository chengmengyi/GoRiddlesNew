package game.riddles.browser.view.web

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import game.riddles.browser.cons.IntentKeys
import game.riddles.browser.factory.sTabManager
import game.riddles.browser.tab.ITab
import game.riddles.browser.tab.WebCaptureCacheFactory
import game.riddles.browser.tab.manager.ITabManager
import game.riddles.browser.tab.manager.TabManager
import game.riddles.core.top.BaseViewData
import game.riddles.core.top.ILayoutLifecycleObserver

class BrowserData(application: Application) : BaseViewData(application), ILayoutLifecycleObserver,
    ITabManager.Callback, ITab.Callback {
    val addOrRemoveTabContentView = MutableLiveData<View?>()
    val startTabManageActivity = MutableLiveData<Boolean>()

    private var mCurTab: ITab? = null
    private var mTabManager: ITabManager? = null

    private fun initTab(tab: ITab) {
        mCurTab?.removeCallback(this)
        mCurTab = tab
        addOrRemoveTabContentView.value = tab.getTabContentView()
        tab.setCallback(this)
    }

    override fun onLayoutCreate(savedInstanceState: Bundle?) {
        initTabManager()
    }

    override fun onLayoutNewIntent(i: Intent?) {
        super.onLayoutNewIntent(i)
        val searchContent = i?.getStringExtra(IntentKeys.SEARCH_KEYWORDS) ?: ""
        if (searchContent.isNotBlank()) {
            searchContent(searchContent)
        }
    }

    private fun searchContent(content: String) {
        mCurTab?.searchContent(content)
    }

    private fun initTabManager() {
        val ctx = mViewBridge?.getBridgeContext() ?: return
        mTabManager = sTabManager
        if (mTabManager == null) {
            val newTabManager = TabManager(ctx)
            (newTabManager as? ILayoutLifecycleObserver)?.let {
                mViewBridge?.addLayoutLifecycleObserver(it)
            }
            newTabManager.setCallback(this)
            mTabManager = newTabManager
            sTabManager = newTabManager
            newTabManager.getCurTab()?.let { tab -> initTab(tab) }
        } else {
            mTabManager?.changeContext(ctx)
        }
    }

    fun onNavBack(view: View) {
        mCurTab?.navBack()
    }

    fun onNavForward(view: View) {
        mCurTab?.navForward()
    }

    fun onNavHome(view: View) {
        mCurTab?.navHome()
    }

    fun onNavTab(view: View) {
        startTabManageActivity.value = true
    }

    fun shouldInterceptBackEvent(): Boolean {
        return mCurTab?.shouldInterceptBackEvent() == true
    }

    override fun onCleared() {
        super.onCleared()
        mTabManager?.removeCallback(this)
        mTabManager?.destroy()
        mCurTab?.removeCallback(this)
        sTabManager = null
        WebCaptureCacheFactory.recycle()
    }

    override fun onCurTabChanged(curTab: ITab) {
        initTab(curTab)
    }

    override fun onTabContentChanged(newContentView: View) {
        addOrRemoveTabContentView.value = newContentView
    }
}