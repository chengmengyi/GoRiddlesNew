package game.riddles.core.utils

import android.content.res.Resources
import com.blankj.utilcode.util.ResourceUtils

fun String.asAssetNameToString(): String {
    val that = this
    return runCatching {
        ResourceUtils.readAssets2String(that)
    }.getOrDefault("")
}

fun Resources.dp2px(dp: Int): Int {
    val scale = displayMetrics.density
    return (dp.toFloat() * scale + 0.5F).toInt()
}