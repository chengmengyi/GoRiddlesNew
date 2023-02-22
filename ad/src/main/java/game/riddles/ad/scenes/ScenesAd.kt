package game.riddles.ad.scenes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import game.riddles.ad.entiti.MAdConfig
import game.riddles.ad.entiti.MAdData
import game.riddles.ad.factory.listTypesAd
import game.riddles.ad.factory.newTpyesAd
import game.riddles.ad.manage.FullScreenManager
import game.riddles.ad.utils.clearSceneData
import game.riddles.ad.utils.printAdLog
import game.riddles.ad.utils.toTypesEnum
import game.riddles.core.utils.FirebaseUtils
import game.riddles.core.utils.toSerializableByJson
import game.riddles.core.utils.asAssetNameToString
import game.riddles.core.utils.isOnResume

internal class ScenesAd(val scene: ScenesEnum) : IScenesAd {
    companion object {
        private var json: String = ""
            get() {
                if (field.isBlank()) {
                    field = "ad_config.json".asAssetNameToString()
                }
                return field
            }

        private val cacheMAdConfig: MAdConfig?
            get() {
                return FirebaseUtils.adConfig.ifBlank { json }
                    .toSerializableByJson()
            }

    }

    private fun loadInternal(context: Context, requestCount: Int = 1) {
        val requestContext = context.applicationContext
        if (scene.isLoading) {
            printAdLog("is requesting")
            return
        }

        val cache = scene.sceneData.data
        val cacheTime = scene.sceneData.createTime
        if (cache != null) {
            if (cacheTime > 0L && System.currentTimeMillis() - cacheTime > 1000L * 60L * 60L) {
                printAdLog("cache is expired")
                scene.clearSceneData()
            } else {
                return
            }
        }

        scene.isLoading = true
        printAdLog("load started")

        loadLoop(requestContext, listOption()) {
            val isSuccessful = it != null
            printAdLog("load complete, result=$isSuccessful")
            if (isSuccessful) {
                scene.sceneData.data = it
                scene.sceneData.createTime = System.currentTimeMillis()
            }
            scene.isLoading = false
            if (!isSuccessful && scene == ScenesEnum.Open && requestCount < 2) {
                loadInternal(requestContext, requestCount + 1)
            }
        }
    }

    override fun load(context: Context) {
        loadInternal(context)
    }

    private fun listOption(): List<MAdData> {
        val config = cacheMAdConfig ?: MAdConfig()
        return when (scene) {
            ScenesEnum.Open -> config.open
            ScenesEnum.Reward -> config.reward
            ScenesEnum.Time -> config.time
        }.sortedByDescending { it.sort }
    }

    private fun loadLoop(
        context: Context,
        options: List<MAdData>,
        startIndex: Int = 0,
        callback: ((result: Any?) -> Unit)
    ) {
        val option = options.getOrNull(startIndex)
        if (option == null) {
            callback(null)
            return
        }
        printAdLog("on request: $option")
        onLoad(context, option) {
            if (it == null)
                loadLoop(context, options, startIndex + 1, callback)
            else
                callback(it)
        }
    }

    private fun onLoad(
        context: Context,
        data: MAdData,
        callback: (result: Any?) -> Unit
    ) {
        val type = data.type.toTypesEnum()
        if (type == null) {
            callback(null)
            return
        }
        newTpyesAd(type).load(scene, context, data, callback)
    }

    override fun show(
        context: AppCompatActivity,
        data: Any,
        callback: IScenesAd.OnSceneAdShowCallback
    ) {
        val type = listTypesAd().firstOrNull { it.isSupported(data) }
        if (type == null
            || !context.lifecycle.isOnResume()
            || (type.isFullScreenType() && FullScreenManager.isShowing)
        ) {
            callback.onAdShowCompleted()
            return
        }
        type.show(scene, context, data, callback)
    }
}