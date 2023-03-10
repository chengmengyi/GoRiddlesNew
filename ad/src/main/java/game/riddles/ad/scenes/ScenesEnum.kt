package game.riddles.ad.scenes

enum class ScenesEnum(
    var isLoading: Boolean = false,
    val sceneData: SceneData = SceneData()
) {
    Open,
    Reward,
    Time
}