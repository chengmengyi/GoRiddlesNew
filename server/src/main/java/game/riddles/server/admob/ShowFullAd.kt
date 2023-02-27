package game.riddles.server.admob

import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import game.riddles.server.base.BaseAc
import game.riddles.server.util.AdShowed
import game.riddles.server.util.logGo

class ShowFullAd(
    private val baseAc: BaseAc,
    private val type:String,
) {

    fun show(emptyBack:Boolean=false,showing:()->Unit,close:()->Unit){
        val adResult = LoadAdImpl.getAdResult(type)
        if (null!=adResult){
            if (LoadAdImpl.showingFull||!baseAc.resume){
                return
            }
            logGo("start show $type ad")
            showing.invoke()
            when(adResult){
                is InterstitialAd ->{
                    adResult.fullScreenContentCallback= FullAdCallback(baseAc, type, close)
                    adResult.show(baseAc)
                }
                is AppOpenAd ->{
                    adResult.fullScreenContentCallback= FullAdCallback(baseAc, type, close)
                    adResult.show(baseAc)
                }
            }
        }else{
            if (emptyBack){
                close.invoke()
            }
        }
    }
}