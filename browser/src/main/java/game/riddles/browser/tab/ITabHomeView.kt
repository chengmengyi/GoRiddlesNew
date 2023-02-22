package game.riddles.browser.tab

import android.graphics.Bitmap
import android.view.View

internal interface ITabHomeView {
    fun onCreate()
    fun getContentView(): View
    fun getCaptureBitmap(): Bitmap?
}