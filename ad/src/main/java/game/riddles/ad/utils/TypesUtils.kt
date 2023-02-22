package game.riddles.ad.utils

import game.riddles.ad.types.TypesEnum

internal fun String.toTypesEnum(): TypesEnum? {
    return when (this) {
        "o" -> TypesEnum.Open
        "i" -> TypesEnum.Inter
        "r" -> TypesEnum.Reward
        else -> null
    }
}