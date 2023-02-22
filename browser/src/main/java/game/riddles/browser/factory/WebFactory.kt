package game.riddles.browser.factory

import game.riddles.browser.web.AndroidWebEngine
import game.riddles.browser.web.IWebEngine

internal fun newWebEngine(): IWebEngine {
    return AndroidWebEngine()
}