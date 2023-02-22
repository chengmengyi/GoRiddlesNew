package game.riddles.ad.utils

import game.riddles.ad.scenes.ScenesAd
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.core.utils.printLog

internal fun ScenesAd.printAdLog(content: String) {
    scene.printAdLog(content)
}

internal fun ScenesEnum.printAdLog(content: String) {
    printAdSceneLog(this.name, content)
}

private fun printAdSceneLog(scene: String, content: String) {
    printLog(content, "AD[${scene}]")
}