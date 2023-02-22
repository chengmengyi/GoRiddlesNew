package game.riddles.ad.admob

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import game.riddles.ad.manage.FullScreenManager
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.ad.utils.clearSceneData
import game.riddles.ad.utils.printAdLog

internal class AdmobFullScreenCallback(
    private val scene: ScenesEnum,
    private val callback: IScenesAd.OnSceneAdShowCallback
) : FullScreenContentCallback() {
    override fun onAdDismissedFullScreenContent() {
        scene.printAdLog("dismissed")
        onAdComplete()
    }

    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
        scene.printAdLog("fail to show, message=${p0.message}]")
        onAdComplete()
    }

    private fun onAdComplete() {
        FullScreenManager.isShowing = false
        scene.clearSceneData()
        callback.onAdShowCompleted()
    }

    override fun onAdShowedFullScreenContent() {
        scene.printAdLog("showed")
    }
}