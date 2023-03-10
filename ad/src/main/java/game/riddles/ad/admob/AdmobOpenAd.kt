package game.riddles.ad.admob

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import game.riddles.ad.entiti.MAdData
import game.riddles.ad.manage.FullScreenManager
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.ad.types.IOpenAd
import game.riddles.ad.utils.printAdLog

internal class AdmobOpenAd : IOpenAd {

    override fun load(
        scene: ScenesEnum,
        context: Context,
        data: MAdData,
        callback: (result: Any?) -> Unit
    ) {
        AppOpenAd.load(
            context,
            data.dataId,
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object :
                AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    scene.printAdLog("request fail: ${loadAdError.message}")
                    callback(null)
                }

                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    TransAdEvent.setLoadAdIpCityName(scene)
                    appOpenAd.setOnPaidEventListener {
                        TransAdEvent.transAdEvent(context,it,appOpenAd.responseInfo,data,scene)
                    }
                    callback(appOpenAd)
                }
            })
    }

    override fun show(
        scene: ScenesEnum,
        context: AppCompatActivity,
        data: Any,
        callback: IScenesAd.OnSceneAdShowCallback
    ) {
        with(data as AppOpenAd) {
            fullScreenContentCallback = AdmobFullScreenCallback(scene, callback)
            FullScreenManager.isShowing = true
            show(context)
        }
    }

    override fun isSupported(data: Any): Boolean {
        return data is AppOpenAd
    }

    override fun isFullScreenType(): Boolean {
        return true
    }
}