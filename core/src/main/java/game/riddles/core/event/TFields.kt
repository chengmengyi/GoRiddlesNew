package game.riddles.core.event

import android.content.Context
import android.os.Build
import android.webkit.WebSettings
import androidx.annotation.Keep
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.blankj.utilcode.util.*
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import game.riddles.core.utils.*
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

    private fun forSession(accept: (fields: JSONObject) -> Unit) {
        forCommon {
            it.put("diet", "muenster")
            accept(it)
        }
    }

    private fun forInstall(accept: (fields: JSONObject) -> Unit) {
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

    private fun forCommon(accept: (fields: JSONObject) -> Unit) {
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