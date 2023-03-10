package game.riddles.ad.admob

import android.content.Context
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo
import game.riddles.ad.entiti.MAdData
import game.riddles.ad.scenes.ScenesEnum
import game.riddles.core.event.TEvent
import game.riddles.server.ServerApi

object TransAdEvent {
    val loadAdIpMap= hashMapOf<String,String>()
    val loadAdCityMap= hashMapOf<String,String?>()


    fun setLoadAdIpCityName(scene: ScenesEnum){
        val type = getAdLocation(scene)
        loadAdIpMap[type]=ServerApi.getCurrentIp()?:""
        loadAdCityMap[type]= ServerApi.getCurrentCityName()
    }

    private fun getAdLocation(scene: ScenesEnum)=when (scene) {
        ScenesEnum.Open -> "enterid_open"
        ScenesEnum.Reward -> "enterid_reward"
        ScenesEnum.Time -> "enterid_time"
    }

    fun transAdEvent(
        context: Context,
        adValue: AdValue,
        responseInfo: ResponseInfo?,
        adBean: MAdData,
        scene: ScenesEnum,
    ){
        val type = getAdLocation(scene)
        TEvent(context).transAdEvent(
            adValue.valueMicros,
            adValue.currencyCode,
            responseInfo?.mediationAdapterClassName?:"",
            adBean.dataId,
            type,
            adBean.type,
            adValue.precisionType.toString(),
            loadAdIpMap[type]?:"",
            ServerApi.getCurrentIp()?:"null",
            loadAdCityMap[type]?:"null",
            ServerApi.getCurrentCityName()?:"null"
        )
    }
}
