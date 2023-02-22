package game.riddles.browser.tab

import android.graphics.Bitmap
import android.view.View

internal interface ITabWebView {
    fun onCreate()
    fun getContentView(): View
    fun navBack()
    fun navForward()
    fun search(content: String)
    fun canGoBack(): Boolean
    fun getWebTitle(): String
    fun getFavIcon(): Bitmap?
    fun getWebCapture(): Bitmap?
}