package game.riddles.core.utils

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient

fun Context.googleAdsId(): String = runCatching {
    AdvertisingIdClient.getAdvertisingIdInfo(this).id
}.getOrElse { "" }

fun Context.firstInstallTime(): Long {
    return packageManager.getPackageInfo(packageName, 0).firstInstallTime
}

fun Context.lastUpdateTime(): Long {
    return packageManager.getPackageInfo(packageName, 0).lastUpdateTime
}