package game.riddles.ad.apis

import android.app.Activity

interface IAdFunProxy {
    fun proxyAdFinishLaunchActivity()
    fun proxyAdStartLaunchActivity(context: Activity)
}