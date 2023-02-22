package game.riddles.question.factory

import game.riddles.question.repository.IQuestionsRepository
import game.riddles.question.repository.IUsrRepository
import game.riddles.question.repository.LocalQuestionsRepository
import game.riddles.question.repository.LocalUsrRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

internal fun newQuestionsRepository(
    scope: CoroutineScope = GlobalScope
): IQuestionsRepository {
    return LocalQuestionsRepository(scope)
}

internal fun newUsrRepository(
    scope: CoroutineScope = GlobalScope
): IUsrRepository {
    return LocalUsrRepository(scope)
}