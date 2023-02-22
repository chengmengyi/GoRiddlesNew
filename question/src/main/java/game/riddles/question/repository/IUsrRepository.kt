package game.riddles.question.repository

internal interface IUsrRepository {
    fun getLevel(): Int
    fun updateLevel(level: Int)
    fun getCoinCount(): Int
    fun updateCoinCount(count: Int)
    fun getLevelExp(): Int
    fun updateLevelExp(exp: Int)
    fun getFreeTipsOpportunity(): Int
    fun updateFreeTipsOpportunity(oppo: Int)

}