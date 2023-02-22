package game.riddles.server.admob

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import game.riddles.server.bean.AdBean
import game.riddles.server.bean.AdResultBean
import game.riddles.server.myApp
import game.riddles.server.util.AdShowed
import game.riddles.server.util.logGo

abstract class BaseLoadAd {

    protected fun loadAdByType(
        type: String,
        adBean: AdBean,
        callback: (result: AdResultBean?) -> Unit
    ) {
        logGo("start load $type ad ,${adBean.toString()}")
        when (adBean.type) {
            "o" ->loadOpen(type, adBean, callback)
            "i" ->loadInterstitial(type, adBean, callback)
            "n" ->loadNative(type, adBean, callback)
        }
    }

    private fun loadOpen(type: String, adBean: AdBean, callback: (result: AdResultBean?) -> Unit) {
        AppOpenAd.load(
            myApp,
            adBean.data_id,
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    logGo("load $type ad success")
                    callback.invoke(AdResultBean(System.currentTimeMillis(), p0))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    logGo("load $type fail,${p0.message}")
                    callback.invoke(null)
                }
            }
        )
    }

    private fun loadInterstitial(
        type: String,
        adBean: AdBean,
        callback: (result: AdResultBean?) -> Unit
    ) {
        InterstitialAd.load(
            myApp,
            adBean.data_id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    logGo("load $type fail,${p0.message}")
                    callback.invoke(null)
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    logGo("load $type ad success")
                    callback.invoke(AdResultBean(System.currentTimeMillis(), p0))
                }
            }
        )
    }

    private fun loadNative(
        type: String,
        adBean: AdBean,
        callback: (result: AdResultBean?) -> Unit
    ) {
        AdLoader.Builder(
            myApp,
            adBean.data_id,
        ).forNativeAd {
            logGo("load $type ad success")
            callback.invoke(AdResultBean(System.currentTimeMillis(), it))
        }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    logGo("load $type fail,${p0.message}")
                    callback.invoke(null)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    AdShowed.addClick()
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(
                        NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                    )
                    .build()
            )
            .build()
            .loadAd(AdRequest.Builder().build())
    }

}