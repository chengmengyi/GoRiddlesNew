package game.riddles.question.usr

import game.riddles.core.utils.forEachRemain
import game.riddles.question.factory.newUsrRepository
import kotlin.math.max

internal object UsrManager : IUsrManager {
    private val usrRepository by lazy { newUsrRepository() }
    private val usrAttrChangedListeners by lazy { mutableSetOf<OnUsrAttrChanged>() }

    override fun getLevel(): Int {
        return usrRepository.getLevel()
    }

    override fun getCoinCount(): Int {
        return usrRepository.getCoinCount()
    }

    override fun deductCoin(count: Int) {
        val nowCoinCount = getCoinCount()
        val newCoinCount = max(0, nowCoinCount - count)
        usrRepository.updateCoinCount(newCoinCount)
        usrAttrChangedListeners.iterator().forEachRemain { it.onCoinCountChanged(newCoinCount) }
    }

    override fun increaseCoin(count: Int) {
        val newCoinCount = getCoinCount() + count
        usrRepository.updateCoinCount(newCoinCount)
        usrAttrChangedListeners.iterator().forEachRemain { it.onCoinCountChanged(newCoinCount) }
    }

    override fun increaseExperience(exp: Int) {
        val nowLevel = getLevel()
        if (nowLevel >= 10) return
        var newLevel = nowLevel
        val nowLevelExp = getLevelExp()
        var newLevelExp = nowLevelExp
        var consumeExp = exp
        while (consumeExp > 0) {
            val toNextLevel = getExpToLevelUp(newLevelExp)
            consumeExp -= toNextLevel
            if (consumeExp >= 0) {
                newLevel++
                newLevelExp = 0
            } else {
                newLevelExp += exp
                break
            }
        }
        if (newLevel > nowLevel) {
            usrRepository.updateLevel(newLevel)
            usrAttrChangedListeners.iterator().forEachRemain { it.onLevelChanged(newLevel) }
        }
        usrRepository.updateLevelExp(newLevelExp)
        usrAttrChangedListeners.iterator().forEachRemain { it.onLevelExpChanged(newLevelExp) }
    }

    private fun getExpToLevelUp(nowLevelExp: Int): Int {
        return 50 - nowLevelExp
    }

    override fun getLevelExp(): Int {
        return usrRepository.getLevelExp()
    }

    override fun getFreeTipsOpportunity(): Int {
        return usrRepository.getFreeTipsOpportunity()
    }

    override fun deductFreeTipsOpportunity() {
        val nowFreeOppo = getFreeTipsOpportunity()
        val newFreeOppo = max(0, nowFreeOppo - 1)
        usrRepository.updateFreeTipsOpportunity(newFreeOppo)
        usrAttrChangedListeners.iterator().forEachRemain { it.onFreeTipsOpportunity(newFreeOppo) }
    }

    override fun addUsrAttrChangeListener(lis: OnUsrAttrChanged) {
        usrAttrChangedListeners.add(lis)
    }

    override fun removeUsrAttrChangeListener(lis: OnUsrAttrChanged) {
        usrAttrChangedListeners.remove(lis)
    }

}