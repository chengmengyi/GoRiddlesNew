package game.riddles.core.event

import android.content.Context
import android.util.Log
import game.riddles.core.ApiServer
import game.riddles.core.net.HttpUtils
import game.riddles.core.utils.CacheableBooleanDelegate
import game.riddles.core.utils.googleAdsId
import game.riddles.core.utils.printLog
import game.riddles.core.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TEvent(private val context: Context) {
    companion object {
        private var installEventUploaded by CacheableBooleanDelegate { "INSTALL_EVENT_UPLOADED" }
    }

    fun transSession() {
        TFields.forSession(context) {
            transEvent("session", it)
        }
    }

    private fun transEvent(
        eventName: String,
        body: JSONObject,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        printLog("=transEvent=$eventName==params==${body.toString()}","Event")
        GlobalScope.launch(Dispatchers.IO) {
            val res = runCatching {
                HttpUtils.retrofit().create(ApiServer.IEvent::class.java)
                    .upload(
                        googleAdsId = context.googleAdsId(),
                        body = body.toString().toRequestBody("text/*; charset=utf-8".toMediaType())
                    )
            }
                .getOrNull()
            if (res != null) {
                printLog("transEvent $eventName success","Event")
//                printLog("response[$eventName]: ${res.toJson()}", "Event")
                onSuccess?.invoke()
            } else {
                printLog("transEvent $eventName fail","Event")
//                printLog("response[$eventName]: error", "Event")
                onFailure?.invoke()
            }
        }
    }

    fun transInstall() {
        if (installEventUploaded) return
        TFields.forInstall(context) {
            transEvent("install", it, onSuccess = {
                installEventUploaded = true
            })
        }
    }

    fun transAdEvent(
        valueMicros:Long,
        currencyCode:String,
        mediationAdapterClassName:String,
        adID:String,
        adLocation:String,
        adType:String,
        precisionType:String,
        loadIp:String,
        showIp:String,
        loadCity:String,
        showCity:String
    ){
        TFields.forAdEvent(
            context,
            valueMicros,
            currencyCode,
            mediationAdapterClassName,
            adID,
            adLocation,
            adType,
            precisionType,
            loadIp,
            showIp,
            loadCity,
            showCity,
            callback = {
                transEvent("ad_event", it)
            }
        )
    }
}