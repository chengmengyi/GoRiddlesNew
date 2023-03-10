package game.riddles.server.bean

import androidx.annotation.Keep

@Keep
data class ConfigServerBean(
    val c: Int?,
    val d: D?,
    val m: String?
)

@Keep
data class D(
    val noun: List<Noun?>?
)

@Keep
data class Noun(
    val anchors: List<Anchor?>?,
    val haculs: String?,
    val trails: String?
)

@Keep
data class Anchor(
    val commission: String?,
    val grinder: Int?,
    val lands: List<String?>?
)