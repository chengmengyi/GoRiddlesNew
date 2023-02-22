package game.riddles.browser.tab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import game.riddles.browser.R
import game.riddles.core.top.ILayoutLifecycleObserver

internal class Tab(context: Context) : ITab, ILayoutLifecycleObserver {
    private lateinit var homeView: ITabHomeView
    private lateinit var webView: ITabWebView
    private var viewIsHome = true
    private var useContext: Context? = context
    private var tabCallback: ITab.Callback? = null

    override fun onLayoutCreate(savedInstanceState: Bundle?) {
        val ctx = useContext ?: return
        homeView = TabHomeView(ctx).apply { this.onCreate() }
        webView = TabWebView(ctx).apply { this.onCreate() }
    }

    override fun getTabContentView(): View {
        return if (viewIsHome) homeView.getContentView() else webView.getContentView()
    }

    override fun navBack() {
        if (viewIsHome) return
        if (webView.canGoBack()) {
            webView.navBack()
        } else {
            navHome()
        }
    }

    override fun navForward() {
        if (viewIsHome) {
            changeContentViewTo(false)
            return
        }
        webView.navForward()
    }

    private fun changeContentViewTo(isHome: Boolean) {
        viewIsHome = isHome
        if (isHome) {
            (webView as? ILayoutLifecycleObserver)?.onLayoutPause()
        } else {
            (webView as? ILayoutLifecycleObserver)?.onLayoutResume()
        }
        tabCallback?.onTabContentChanged(getTabContentView())
    }

    override fun navHome() {
        if (viewIsHome) return
        changeContentViewTo(true)
    }

    override fun changeContext(newCtx: Context) {
        useContext = newCtx
    }

    override fun searchContent(content: String) {
        if (viewIsHome) {
            viewIsHome = false
            tabCallback?.onTabContentChanged(getTabContentView())
        }
        webView.search(content)
    }

    override fun setCallback(cb: ITab.Callback) {
        tabCallback = cb
    }

    override fun removeCallback(cb: ITab.Callback) {
        tabCallback = null
    }

    override fun getWebTitle(): String {
        return webView.getWebTitle().ifBlank { "New Tab" }
    }

    override fun getFavIcon(): Bitmap? {
        var favicon = webView.getFavIcon()
        if (favicon == null) {
            favicon = useContext?.run {
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_browser_logo
                )
            }
        }
        return favicon
    }

    override fun getWebCapture(): Bitmap? {
        return if (viewIsHome) homeView.getCaptureBitmap() else webView.getWebCapture()
    }

    override fun shouldInterceptBackEvent(): Boolean {
        if (viewIsHome) return false
        if (webView.canGoBack()) {
            webView.navBack()
            return true
        }
        return false
    }

    override fun onLayoutPause() {
        if (viewIsHome) return
        (webView as? ILayoutLifecycleObserver)?.onLayoutPause()
    }

    override fun onLayoutResume() {
        if (viewIsHome) return
        (webView as? ILayoutLifecycleObserver)?.onLayoutResume()
    }

    override fun onLayoutDestroy() {
        (webView as? ILayoutLifecycleObserver)?.onLayoutDestroy()
        tabCallback = null
    }
}