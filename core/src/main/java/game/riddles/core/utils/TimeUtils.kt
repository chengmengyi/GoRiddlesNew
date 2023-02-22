package game.riddles.core.utils

import java.util.*

fun timeZone(timestamp: Long = System.currentTimeMillis()): Int =
    TimeZone.getDefault().getOffset(timestamp) / 3600000