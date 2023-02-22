package game.riddles.core.utils

import android.util.Log
import game.riddles.core.BuildConfig

fun printLog(content: String, tag: String) {
    if (!BuildConfig.DEBUG) return
    Log.e("${BuildConfig.LIBRARY_PACKAGE_NAME}.${tag}", content)
}