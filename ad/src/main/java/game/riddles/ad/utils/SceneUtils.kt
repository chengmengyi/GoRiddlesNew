package game.riddles.ad.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import game.riddles.ad.factory.newScenesAd
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum

internal val ScenesEnum.result: Any?
    get() {
        return sceneData.data
    }

internal fun ScenesEnum.load(context: Context) {
    newScenesAd(this).load(context)
}

internal fun ScenesEnum.show(
    context: AppCompatActivity,
    data: Any,
    callback: IScenesAd.OnSceneAdShowCallback
) {
    newScenesAd(this).show(context, data, callback)
}

internal fun ScenesEnum.clearSceneData() {
    sceneData.createTime = 0L
    sceneData.data = null
}
