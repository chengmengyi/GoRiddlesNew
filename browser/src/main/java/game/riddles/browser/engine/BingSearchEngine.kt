package game.riddles.browser.engine

import android.graphics.drawable.Drawable

internal object BingSearchEngine : ISearchEngine {
    override fun getIcon(): Drawable? {
        return null
    }

    override fun getSearchUrl(): String {
        return "https://cn.bing.com/search?q="
    }

    override fun getHomeUrl(): String {
        return "https://cn.bing.com/"
    }
}