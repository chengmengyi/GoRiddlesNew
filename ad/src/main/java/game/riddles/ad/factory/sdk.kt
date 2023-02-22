package game.riddles.ad.factory

import game.riddles.ad.admob.AdmobSdk
import game.riddles.ad.sdk.IAdSdk

internal fun newAdSdk(): IAdSdk {
    return AdmobSdk
}