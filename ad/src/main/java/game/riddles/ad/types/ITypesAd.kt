package game.riddles.ad.types

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import game.riddles.ad.entiti.MAdData
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum

internal interface ITypesAd {
    fun load(scene: ScenesEnum, context: Context, data: MAdData, callback: (result: Any?) -> Unit)
    fun show(
        scene: ScenesEnum,
        context: AppCompatActivity,
        data: Any,
        callback: IScenesAd.OnSceneAdShowCallback
    )

    fun isSupported(data: Any): Boolean
    fun isFullScreenType(): Boolean
}