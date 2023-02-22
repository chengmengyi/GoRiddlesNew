package game.riddles.question.utils

import game.riddles.question.R

internal fun checkLevelValue(level: Int): Int {
    return if (level < 1 || level > 10) throw IllegalArgumentException("usrLevel must be in 1..10") else level
}

internal fun getLevelIconRes(level: Int): Int {
    return when (checkLevelValue(level)) {
        1 -> R.mipmap.level_1
        2 -> R.mipmap.level_2
        3 -> R.mipmap.level_3
        4 -> R.mipmap.level_4
        5 -> R.mipmap.level_5
        6 -> R.mipmap.level_6
        7 -> R.mipmap.level_7
        8 -> R.mipmap.level_8
        9 -> R.mipmap.level_9
        10 -> R.mipmap.level_10
        else -> -1
    }
}