package game.riddles.question.repository

import game.riddles.core.top.BaseRepository
import game.riddles.core.utils.CacheableIntDelegate
import game.riddles.question.utils.checkLevelValue
import kotlinx.coroutines.CoroutineScope
import kotlin.math.max

internal class LocalUsrRepository(scope: CoroutineScope) : BaseRepository(scope), IUsrRepository {
    private var usrLevel by CacheableIntDelegate { "USR_LEVEL" }
    private var usrCoinCount by CacheableIntDelegate { "USR_COIN_COUNT" }
    private var usrLevelExp by CacheableIntDelegate { "USR_LEVEL_EXP" }
    private var usrFreeTipsOppo by CacheableIntDelegate(def = 3) { "USR_FREE_OPPO" }

    override fun getLevel(): Int {
        return max(1, usrLevel)
    }

    override fun updateLevel(level: Int) {
        usrLevel = checkLevelValue(level)
    }

    override fun getCoinCount(): Int {
        return usrCoinCount
    }

    override fun updateCoinCount(count: Int) {
        usrCoinCount = count
    }

    override fun getLevelExp(): Int {
        return usrLevelExp
    }

    override fun updateLevelExp(exp: Int) {
        usrLevelExp = exp
    }

    override fun getFreeTipsOpportunity(): Int {
        return usrFreeTipsOppo
    }

    override fun updateFreeTipsOpportunity(oppo: Int) {
        usrFreeTipsOppo = oppo
    }
}