package game.riddles.question.repository

import game.riddles.core.top.BaseRepository
import game.riddles.core.utils.toSerializableByJson
import game.riddles.core.utils.asAssetNameToString
import game.riddles.core.utils.randomSubList
import game.riddles.question.entity.MQuestion
import game.riddles.question.entity.MQuestionLib
import game.riddles.question.utils.checkLevelValue
import game.riddles.question.utils.isAnswered
import kotlinx.coroutines.CoroutineScope

internal class LocalQuestionsRepository(scope: CoroutineScope) : BaseRepository(scope),
    IQuestionsRepository {

    override fun listQuestionsByUsrLevel(usrLevel: Int): List<MQuestion> {
        val usrLevelLessThan6 = checkLevelValue(usrLevel) <= 5
        return (if (usrLevelLessThan6) "questions_level_1_5.json" else "questions_level_6_10.json")
            .asAssetNameToString()
            .toSerializableByJson<MQuestionLib>()!!
            .list
            .filterNot { it.isAnswered() }
            .randomSubList(if (usrLevelLessThan6) 50 else 25)
            .map {
                it.copy(options = it.options.randomSubList(it.options.size))
            }
    }
}