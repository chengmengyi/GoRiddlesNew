package game.riddles.browser.web

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

internal interface IWebEngine {

    fun addWebView(container: ViewGroup)
    fun loadUrl(url: String)
    fun back()
    fun canGoBack(): Boolean
    fun forward()
    fun reload()
    fun getWebTitle(): String
    fun getFavIcon(): Bitmap?
    fun getWebCapture(): Bitmap?
    fun addWebStateChangedListener(lis: OnWebStateChanged)
    fun removeWebStateChangedListener(lis: OnWebStateChanged)
}