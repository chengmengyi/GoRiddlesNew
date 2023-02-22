package game.riddles

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import game.riddles.ad.apis.IAdFunProxy
import game.riddles.ad.apis.SceneAdsApi
import game.riddles.core.top.BaseApplication
import game.riddles.server.ServerApi

class GoRiddles : BaseApplication(), IAdFunProxy {
    override fun initVpnCore() {
        ServerApi.initVpnCore(this)
    }

    override fun onMainProcessCreate() {
        super.onMainProcessCreate()
        SceneAdsApi.init(this, this)
        ServerApi.init(this)
        ServerApi.readReferrer(this)
    }

    override fun proxyAdFinishLaunchActivity() {
        ActivityUtils.finishActivity(SplashActivity::class.java)
    }

    override fun proxyAdStartLaunchActivity(context: Activity) {
        context.startActivity(Intent(context, SplashActivity::class.java))
    }
}