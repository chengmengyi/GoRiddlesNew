package game.riddles.ad.factory

import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesAd
import game.riddles.ad.scenes.ScenesEnum

private val objMap = hashMapOf<ScenesEnum, IScenesAd>()

internal fun newScenesAd(scene: ScenesEnum): IScenesAd {
    return objMap.getOrPut(scene) { ScenesAd(scene) }
}