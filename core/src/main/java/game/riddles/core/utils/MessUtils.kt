package game.riddles.core.utils

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.googleAdsId(): String = withContext(Dispatchers.IO) {
    runCatching {
        AdvertisingIdClient.getAdvertisingIdInfo(this@googleAdsId).id
    }.getOrElse { "" }
}

fun Context.firstInstallTime(): Long {
    return packageManager.getPackageInfo(packageName, 0).firstInstallTime
}

fun Context.lastUpdateTime(): Long {
    return packageManager.getPackageInfo(packageName, 0).lastUpdateTime
}