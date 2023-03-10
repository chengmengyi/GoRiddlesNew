package game.riddles.core.event

import android.content.Context
import android.os.Build
import android.webkit.WebSettings
import androidx.annotation.Keep
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.blankj.utilcode.util.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import game.riddles.core.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.Serializable
import java.util.*

internal class TFields private constructor(private val context: Context) {
    companion object {
        fun forSession(context: Context, accept: (fields: JSONObject) -> Unit) {
            TFields(context).forSession(accept)
        }

        fun forInstall(context: Context, accept: (fields: JSONObject) -> Unit) {
            TFields(context).forInstall(accept)
        }

        fun forAdEvent(
            context: Context,
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
            showCity:String,
            callback: (json: JSONObject) -> Unit
            ){
            TFields(context).forAdEvent(valueMicros, currencyCode, mediationAdapterClassName, adID, adLocation, adType, precisionType, loadIp, showIp, loadCity, showCity,callback)
        }

        private var googleReferrerCopyJson by CacheableStringDelegate { "GOOGLE_REFERRER" }
    }

    private val isGoogleAdsTrackLimited: Boolean
        get() {
            return runCatching {
                AdvertisingIdClient.getAdvertisingIdInfo(context).isLimitAdTrackingEnabled
            }.getOrElse { false }
        }

    @Keep
    data class GoogleReferrerCopy(
        val referrer: String,
        val version: String,
        val clickClientTimestamp: Long,
        val beginClientTimestamp: Long,
        val clickServerTimestamp: Long,
        val beginServerTimestamp: Long,
        val instant: Boolean
    ) : Serializable

    private fun forAdEvent(
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
        showCity:String,
        callback:(json:JSONObject)->Unit
        ){
        GlobalScope.launch(Dispatchers.IO) {
            forCommon {
                val jsonObject = JSONObject()
                jsonObject.put("depart",valueMicros)
                jsonObject.put("solar",currencyCode)
                jsonObject.put("billion",getAdNetWork(mediationAdapterClassName))
                jsonObject.put("rumpus","admob")
                jsonObject.put("hardtack",adID)
                jsonObject.put("smooth",adLocation)
                jsonObject.put("slump","")
                jsonObject.put("glucose",getAdType(adType))
                jsonObject.put("sienna",precisionType)
                jsonObject.put("packard",loadIp)
                jsonObject.put("stumpy",showIp)

                val ruse = JSONObject()
                ruse.put("flash_request_city",loadCity)
                ruse.put("flash_show_city",showCity)
                it.put("zest",jsonObject)
                it.put("ruse",ruse)
                callback.invoke(it)
            }
        }
    }


    private fun getAdType(adType: String):String{
        when(adType){
            "o"->return "open"
            "i"->return "Interstitial"
            "n"->return "native"
        }
        return ""
    }

    private fun getAdNetWork(string: String):String{
        if(string.contains("facebook")) return "facebook"
        else if(string.contains("admob")) return "admob"
        return ""
    }

    private fun forSession(accept: (fields: JSONObject) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            forCommon {
                it.put("diet", "muenster")
                accept(it)
            }
        }
    }

    private fun forInstall(accept: (fields: JSONObject) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            forCommon { common ->
                getGoogleReferrer {
                    accept(
                        common.put(
                            "jesus", JSONObject()
                                .put("dovekie", "build/${Build.VERSION.RELEASE}")
                                .put("drum", it.referrer)
                                .put("parcel", it.version)
                                .put("lymph", WebSettings.getDefaultUserAgent(context))
                                .put("diadem", if (isGoogleAdsTrackLimited) "park" else "deathbed")
                                .put("fantasia", it.clickClientTimestamp)
                                .put("sickle", it.beginClientTimestamp)
                                .put("commune", it.clickServerTimestamp)
                                .put("cometh", it.beginServerTimestamp)
                                .put("olefin", context.firstInstallTime())
                                .put("ocelot", context.lastUpdateTime())
                                .put("boutique", it.instant)
                        )
                    )
                }
            }
        }
    }



    private fun getGoogleReferrer(accept: (referrer: GoogleReferrerCopy) -> Unit) {
        val fromCache = googleReferrerCopyJson.toSerializableByJson<GoogleReferrerCopy>()
        if (fromCache != null) {
            accept(fromCache)
            return
        }
        runCatching {
            InstallReferrerClient.newBuilder(context).build().apply {
                startConnection(object : InstallReferrerStateListener {
                    override fun onInstallReferrerSetupFinished(p0: Int) {
                        when (p0) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                accept(GoogleReferrerCopy(
                                    referrer = installReferrer.installReferrer ?: "",
                                    version = installReferrer.installVersion ?: "",
                                    clickClientTimestamp = installReferrer.referrerClickTimestampSeconds,
                                    clickServerTimestamp = installReferrer.referrerClickTimestampServerSeconds,
                                    beginClientTimestamp = installReferrer.installBeginTimestampSeconds,
                                    beginServerTimestamp = installReferrer.installBeginTimestampServerSeconds,
                                    instant = installReferrer.googlePlayInstantParam
                                ).apply { googleReferrerCopyJson = toJson() })
                            }
                        }
                        endConnection()
                    }

                    override fun onInstallReferrerServiceDisconnected() {
                    }

                })
            }
        }
    }

    private suspend fun forCommon(accept: (fields: JSONObject) -> Unit) {
        accept(
            JSONObject()
                .put(
                    "azimuth", JSONObject()
                        .put("grouse", context.netType())
                        .put("knives", timeZone())
                        .put("hardcopy", context.googleAdsId())
                        .put("neonate", LanguageUtils.getSystemLanguage().country)
                        .put("pool", LanguageUtils.getSystemLanguage().toString())
                        .put("charcoal", RomUtils.getRomInfo().version)
                        .put("forsythe", context.resources.displayMetrics.densityDpi.toString())
                        .put("tubule", DeviceUtils.getAndroidID())
                        .put("textron", AppUtils.getAppVersionName())
                        .put("suppress", "exotic")
                        .put("dortmund", DeviceUtils.getManufacturer())
                        .put("cactus", UUID.randomUUID().toString())
                        .put("attend", DeviceUtils.getUniqueDeviceId())
                        .put("shasta", "com.goriddles.ingenuity.answer")
                        .put("hover", DeviceUtils.getManufacturer())
                        .put("palmate", System.currentTimeMillis())
                        .put("biota", 0L)
                        .put("transfix", "")
                        .put("tassel", DeviceUtils.getModel())
                        .put("madhya", NetworkUtils.getIPAddress(true))
                        .put("trianon", UUID.randomUUID().toString())
                        .put("reagan", "")
                )
        )
    }
}