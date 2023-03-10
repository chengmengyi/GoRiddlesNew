package game.riddles.ad.apis

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import game.riddles.ad.factory.newAdSdk
import game.riddles.ad.scenes.IScenesAd
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.ad.utils.AppHotUtils
import game.riddles.ad.utils.load
import game.riddles.ad.utils.result
import game.riddles.ad.utils.show

object SceneAdsApi {


    internal lateinit var funProxy: IAdFunProxy
        private set

    interface OnSceneAdShowCallback {
        fun onAdNotSupported() {}
        fun onAdShowCompleted() {}
        fun onAdReward() {}
    }

    @JvmStatic
    fun init(app: Application, funProxy: IAdFunProxy) {
        this.funProxy = funProxy
        AppHotUtils.init(app)
        newAdSdk().initialize(app)
        loadOpen(app)
        loadTime(app)
        loadReward(app)
    }

    @JvmStatic
    fun showOpen(context: AppCompatActivity, data: Any, callback: OnSceneAdShowCallback) {
        ScenesEnum.Open.show(context, data, wrapOnSceneAdShowCallback(callback))
    }

    @JvmStatic
    fun loadOpen(context: Context) {
        ScenesEnum.Open.load(context)
    }

    @JvmStatic
    fun resultOpen(): Any? {
        return ScenesEnum.Open.result
    }

    @JvmStatic
    fun showTime(context: AppCompatActivity, data: Any, callback: OnSceneAdShowCallback) {
        ScenesEnum.Time.show(context, data, wrapOnSceneAdShowCallback(callback))
    }

    @JvmStatic
    fun loadTime(context: Context) {
        ScenesEnum.Time.load(context)
    }

    @JvmStatic
    fun resultTime(): Any? {
        return ScenesEnum.Time.result
    }

    @JvmStatic
    fun showReward(context: AppCompatActivity, data: Any, callback: OnSceneAdShowCallback) {
        ScenesEnum.Reward.show(context, data, wrapOnSceneAdShowCallback(callback))
    }

    @JvmStatic
    fun loadReward(context: Context) {
        ScenesEnum.Reward.load(context)
    }

    @JvmStatic
    fun resultReward(): Any? {
        return ScenesEnum.Reward.result
    }

    private fun wrapOnSceneAdShowCallback(callback: OnSceneAdShowCallback): IScenesAd.OnSceneAdShowCallback {
        return object : IScenesAd.OnSceneAdShowCallback {
            override fun onAdNotSupported() {
                callback.onAdNotSupported()
            }

            override fun onAdReward() {
                callback.onAdReward()
            }

            override fun onAdShowCompleted() {
                callback.onAdShowCompleted()
            }
        }
    }
}