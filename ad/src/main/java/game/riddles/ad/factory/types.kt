package game.riddles.ad.factory

import game.riddles.ad.admob.AdmobInterAd
import game.riddles.ad.admob.AdmobOpenAd
import game.riddles.ad.admob.AdmobRewardAd
import game.riddles.ad.types.ITypesAd
import game.riddles.ad.types.TypesEnum

private val admobOpen by lazy { AdmobOpenAd() }
private val admobInter by lazy { AdmobInterAd() }
private val admobReward by lazy { AdmobRewardAd() }

internal fun newTpyesAd(type: TypesEnum): ITypesAd {
    return when (type) {
        TypesEnum.Open -> admobOpen
        TypesEnum.Inter -> admobInter
        TypesEnum.Reward -> admobReward
    }
}

internal fun listTypesAd(): List<ITypesAd> {
    return listOf(admobOpen, admobInter, admobReward)
}