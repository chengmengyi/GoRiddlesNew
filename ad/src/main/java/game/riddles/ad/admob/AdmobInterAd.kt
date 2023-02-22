package game.riddles.ad.admob

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import game.riddles.ad.entiti.MAdData
import game.riddles.ad.manage.FullScreenManager
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.ad.types.IInterAd
import game.riddles.ad.utils.printAdLog

internal class AdmobInterAd : IInterAd {
    override fun load(
        scene: ScenesEnum,
        context: Context,
        data: MAdData,
        callback: (result: Any?) -> Unit
    ) {
        InterstitialAd.load(
            context,
            data.dataId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    scene.printAdLog("request fail: ${loadAdError.message}")
                    callback(null)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    callback(interstitialAd)
                }
            }
        )
    }

    override fun show(
        scene: ScenesEnum,
        context: AppCompatActivity,
        data: Any,
        callback: IScenesAd.OnSceneAdShowCallback
    ) {
        with(data as InterstitialAd) {
            fullScreenContentCallback = AdmobFullScreenCallback(scene, callback)
            FullScreenManager.isShowing = true
            show(context)
        }
    }

    override fun isSupported(data: Any): Boolean {
        return data is InterstitialAd
    }

    override fun isFullScreenType(): Boolean {
        return true
    }
}