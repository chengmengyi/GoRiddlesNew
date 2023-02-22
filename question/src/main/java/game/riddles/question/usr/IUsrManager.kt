package game.riddles.question.usr

internal interface IUsrManager {
    fun getLevel(): Int
    fun getCoinCount(): Int
    fun deductCoin(count: Int)
    fun increaseCoin(count: Int)
    fun increaseExperience(exp: Int)
    fun getLevelExp(): Int
    fun getFreeTipsOpportunity(): Int
    fun deductFreeTipsOpportunity()
    fun addUsrAttrChangeListener(lis: OnUsrAttrChanged)
    fun removeUsrAttrChangeListener(lis: OnUsrAttrChanged)
}