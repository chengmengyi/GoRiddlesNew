package game.riddles.server.bean

data class ConfigServerBean(
    val c: Int?,
    val d: D?,
    val m: String?
)

data class D(
    val noun: List<Noun?>?
)

data class Noun(
    val anchors: List<Anchor?>?,
    val haculs: String?,
    val trails: String?
)

data class Anchor(
    val commission: String?,
    val grinder: Int?,
    val lands: List<String?>?
)