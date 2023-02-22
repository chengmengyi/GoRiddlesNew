package game.riddles.browser.tab

import android.content.Context
import android.graphics.Bitmap
import android.view.View

interface ITab {
    fun getTabContentView(): View
    fun navBack()
    fun navForward()
    fun navHome()
    fun changeContext(newCtx: Context)
    fun searchContent(content: String)
    fun setCallback(cb: Callback)
    fun removeCallback(cb: Callback)
    fun getWebTitle(): String
    fun getFavIcon(): Bitmap?
    fun getWebCapture(): Bitmap?
    fun shouldInterceptBackEvent(): Boolean

    interface Callback {
        fun onTabContentChanged(newContentView: View)
    }
}