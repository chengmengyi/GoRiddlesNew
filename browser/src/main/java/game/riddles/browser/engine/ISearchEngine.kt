package game.riddles.browser.engine

import android.graphics.drawable.Drawable

internal interface ISearchEngine {
    fun getIcon(): Drawable?
    fun getSearchUrl(): String
    fun getHomeUrl(): String
}