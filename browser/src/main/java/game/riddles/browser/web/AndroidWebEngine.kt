package game.riddles.browser.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import game.riddles.browser.R
import game.riddles.core.utils.addViewFromLayout
import game.riddles.core.utils.dp2px
import game.riddles.core.utils.toBitmap

internal class AndroidWebEngine : BaseWebEngine() {
    private var webView: WebView? = null
    private var webProgress: ProgressBar? = null
    private var facIcon: Bitmap? = null
    private var webTitle = ""
    private var cacheCaptureBitmap: Bitmap? = null

    override fun addWebView(container: ViewGroup) {
        container.addViewFromLayout(R.layout.layout_android_web_view).apply {
            webView = findViewById(R.id.web_view)
            webProgress = findViewById(R.id.web_progress)
            initWebSettings()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebSettings() {
        webView?.settings?.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            runCatching { layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING }
            mediaPlaybackRequiresUserGesture = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            domStorageEnabled = true
            setAppCacheEnabled(true)
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            allowContentAccess = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = false
            allowUniversalAccessFromFileURLs = false
        }
        webView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                updateWebProgress(newProgress)
                notifyWebStateChanged { it.onLoadProgressChanged(newProgress) }
                if (newProgress >= 80) {
                    cacheCaptureBitmap = null
                }
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                facIcon = icon
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                webTitle = title ?: ""
            }
        }
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                notifyWebStateChanged { it.onLoadComplete(url ?: "") }
            }
        }
    }

    private fun updateWebProgress(newProgress: Int) {
        val wp = webProgress ?: return
        wp.visibility = if (newProgress >= 100) View.GONE else View.VISIBLE
        if (newProgress < 100) {
            wp.progress = newProgress
        }
    }

    override fun loadUrl(url: String) {
        webView?.loadUrl(url)
    }

    override fun back() {
        webView?.goBack()
    }

    override fun canGoBack(): Boolean {
        return webView?.canGoBack() ?: false
    }

    override fun forward() {
        webView?.goForward()
    }

    override fun reload() {
        webView?.reload()
    }

    override fun getWebTitle(): String {
        return webTitle.ifEmpty { webView?.url ?: "" }
    }

    override fun getFavIcon(): Bitmap? {
        return facIcon
    }

    override fun getWebCapture(): Bitmap? {
        if (cacheCaptureBitmap == null) {
            val width = webView?.context?.resources?.dp2px(360) ?: return null
            val height = webView?.context?.resources?.dp2px(600) ?: return null
            cacheCaptureBitmap = webView?.capturePicture()?.toBitmap(width = width, height = height)
        }
        return cacheCaptureBitmap
    }

    override fun onLayoutPause() {
        webView?.apply {
            onPause()
            pauseTimers()
        }
    }

    override fun onLayoutResume() {
        webView?.apply {
            onResume()
            resumeTimers()
        }
    }

    override fun onLayoutDestroy() {
        webView?.apply {
            stopLoading()
            destroy()
        }
        cacheCaptureBitmap = null
    }
}