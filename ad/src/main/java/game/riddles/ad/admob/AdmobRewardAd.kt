package game.riddles.ad.admob

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import game.riddles.ad.entiti.MAdData
import game.riddles.ad.manage.FullScreenManager
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.ad.types.IRewardAd
import game.riddles.ad.utils.printAdLog

internal class AdmobRewardAd : IRewardAd {
    override fun load(
        scene: ScenesEnum,
        context: Context,
        data: MAdData,
        callback: (result: Any?) -> Unit
    ) {
        RewardedInterstitialAd.load(
            context,
            data.dataId,
            AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    TransAdEvent.setLoadAdIpCityName(scene)
                    ad.setOnPaidEventListener {
                        TransAdEvent.transAdEvent(context,it,ad.responseInfo,data,scene)
                    }
                    callback(ad)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    scene.printAdLog("request fail: ${adError.message}")
                    callback(null)
                }
            })
    }

    override fun show(
        scene: ScenesEnum,
        context: AppCompatActivity,
        data: Any,
        callback: IScenesAd.OnSceneAdShowCallback
    ) {
        with(data as RewardedInterstitialAd) {
            fullScreenContentCallback = AdmobFullScreenCallback(scene, callback)
            FullScreenManager.isShowing = true
            show(context) {
                scene.printAdLog("get reward: amount=${it.amount}, type=${it.type}")
                callback.onAdReward()
            }
        }
    }

    override fun isSupported(data: Any): Boolean {
        return data is RewardedInterstitialAd
    }

    override fun isFullScreenType(): Boolean {
        return true
    }
}