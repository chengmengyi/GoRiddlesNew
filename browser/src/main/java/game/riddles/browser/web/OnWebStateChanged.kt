package game.riddles.browser.web

internal interface OnWebStateChanged {
    fun onLoadProgressChanged(newProgress: Int) {}
    fun onLoadComplete(url: String) {}
}