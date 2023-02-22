package game.riddles.question.usr

internal interface OnUsrAttrChanged {
    fun onLevelChanged(level: Int) {}
    fun onCoinCountChanged(coinCount: Int) {}
    fun onLevelExpChanged(exp: Int) {}
    fun onFreeTipsOpportunity(freeTipsOpportunity: Int) {}
}