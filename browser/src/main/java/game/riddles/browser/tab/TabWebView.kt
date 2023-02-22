package game.riddles.browser.tab

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.blankj.utilcode.util.KeyboardUtils
import game.riddles.browser.R
import game.riddles.browser.engine.BingSearchEngine
import game.riddles.browser.engine.ISearchEngine
import game.riddles.browser.web.AndroidWebEngine
import game.riddles.browser.web.IWebEngine
import game.riddles.browser.web.OnWebStateChanged
import game.riddles.core.top.ILayoutLifecycleObserver
import game.riddles.core.utils.adaptStatusBar
import game.riddles.core.utils.applySingleDenouncingClick
import game.riddles.core.utils.layout2View
import game.riddles.core.utils.setEditorActionCallback

internal class TabWebView(private val context: Context) : ITabWebView, ILayoutLifecycleObserver,
    OnWebStateChanged {
    private lateinit var myContentView: View
    private lateinit var webViewContainer: LinearLayoutCompat
    private lateinit var btnRefresh: AppCompatImageView
    private lateinit var btnNavHome: AppCompatImageView
    private lateinit var etSearchContent: AppCompatEditText
    private val webEngine: IWebEngine = AndroidWebEngine()

    private var lastSearchContent = ""

    override fun onCreate() {
        myContentView = context.layout2View(R.layout.layout_tab_web)
        myContentView.findViewById<View>(R.id.top_layout).adaptStatusBar()
        webViewContainer =
            myContentView.findViewById<LinearLayoutCompat>(R.id.tab_web_view_container).apply {
                webEngine.addWebView(this)
            }
        btnRefresh = myContentView.findViewById<AppCompatImageView>(R.id.btn_nav_refresh).apply {
            applySingleDenouncingClick { webEngine.reload() }
        }
        btnNavHome = myContentView.findViewById<AppCompatImageView>(R.id.btn_nav_home).apply {
            applySingleDenouncingClick { webEngine.loadUrl(getSearchEngine().getHomeUrl()) }
        }
        etSearchContent = myContentView.findViewById(R.id.et_search_content)
        initSearchEt()
        webEngine.addWebStateChangedListener(this)
    }

    private fun initSearchEt() {
        etSearchContent.setEditorActionCallback(
            onSearchAction = {
                val content = etSearchContent.text?.toString() ?: ""
                if (content.isNotBlank()) {
                    KeyboardUtils.hideSoftInput(etSearchContent)
                    search(content)
                }
            }
        )
    }

    override fun getContentView(): View {
        return myContentView
    }

    override fun navBack() {
        webEngine.back()
    }

    override fun navForward() {
        webEngine.forward()
    }

    override fun search(content: String) {
        val secContent = content.replace("\n", "")
        etSearchContent.setText(secContent)
        lastSearchContent = secContent
        webEngine.loadUrl("${getSearchEngine().getSearchUrl()}${secContent}")
        if (btnRefresh.visibility == View.GONE) {
            btnRefresh.visibility = View.VISIBLE
        }
    }

    override fun canGoBack(): Boolean {
        return webEngine.canGoBack()
    }

    override fun getWebTitle(): String {
        return webEngine.getWebTitle()
    }

    override fun getFavIcon(): Bitmap? {
        return webEngine.getFavIcon()
    }

    override fun getWebCapture(): Bitmap? {
        return webEngine.getWebCapture()
    }

    private fun getSearchEngine(): ISearchEngine {
        return BingSearchEngine
    }

    override fun onLayoutPause() {
        super.onLayoutPause()
        (webEngine as? ILayoutLifecycleObserver)?.onLayoutPause()
    }

    override fun onLayoutResume() {
        super.onLayoutResume()
        (webEngine as? ILayoutLifecycleObserver)?.onLayoutResume()
    }

    override fun onLayoutDestroy() {
        super.onLayoutDestroy()
        (webEngine as? ILayoutLifecycleObserver)?.onLayoutDestroy()
        (context as? Activity)?.let {
            KeyboardUtils.unregisterSoftInputChangedListener(it.window)
        }
        webEngine.removeWebStateChangedListener(this)
    }

    override fun onLoadComplete(url: String) {
        if (!url.contains(getSearchEngine().getSearchUrl())) {
            etSearchContent.setText(webEngine.getWebTitle())
        }
    }
}