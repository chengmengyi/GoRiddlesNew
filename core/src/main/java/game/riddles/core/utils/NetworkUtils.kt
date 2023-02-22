package game.riddles.core.utils

import android.content.Context
import com.blankj.utilcode.util.NetworkUtils

fun Context.netType(): String = runCatching {
    NetworkUtils.getNetworkType().name.replace("NETWORK_", "").lowercase()
}.getOrElse { "" }