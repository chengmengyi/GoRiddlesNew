package game.riddles.ad.admob

import android.content.Context
import com.blankj.utilcode.util.ActivityUtils
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.MobileAds
import game.riddles.ad.sdk.IAdSdk

internal object AdmobSdk : IAdSdk {
    override fun initialize(context: Context) {
        MobileAds.initialize(context)
    }

    override fun finishAdActivity() {
        ActivityUtils.finishActivity(AdActivity::class.java)
    }
}