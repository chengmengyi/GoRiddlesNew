package game.riddles.question.repository

import game.riddles.question.entity.MQuestion
import game.riddles.question.factory.newUsrManager

internal interface IQuestionsRepository {
    fun listQuestionsByUsrLevel(usrLevel: Int = newUsrManager().getLevel()): List<MQuestion>
}