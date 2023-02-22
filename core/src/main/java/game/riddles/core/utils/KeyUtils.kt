package game.riddles.core.utils

import game.riddles.core.BuildConfig

fun String.toUniqueKey(): String {
    return "${BuildConfig.LIBRARY_PACKAGE_NAME}.${this}"
}