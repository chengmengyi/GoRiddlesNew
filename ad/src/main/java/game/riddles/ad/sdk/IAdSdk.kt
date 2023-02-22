package game.riddles.ad.sdk

import android.content.Context

internal interface IAdSdk {
    fun initialize(context: Context)
    fun finishAdActivity()
}