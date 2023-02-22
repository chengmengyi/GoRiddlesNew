package game.riddles.browser.tab

import android.graphics.Bitmap

object WebCaptureCacheFactory {
    private var webHomeCapture: Bitmap? = null

    fun updateWebHomeCapture(bitmap: Bitmap) {
        if (webHomeCapture == null) {
            synchronized(this) {
                if (webHomeCapture == null) {
                    webHomeCapture = bitmap
                }
            }
        }
    }

    fun getWebHomeCapture(): Bitmap? {
        return webHomeCapture
    }

    fun recycle() {
        webHomeCapture?.recycle()
        webHomeCapture = null
    }
}