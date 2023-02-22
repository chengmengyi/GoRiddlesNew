package game.riddles.question.utils

import game.riddles.core.utils.readBooleanFromCache
import game.riddles.core.utils.writeBooleanToCache
import game.riddles.question.entity.MQuestion

internal fun MQuestion.isAnswered(): Boolean {
    return readBooleanFromCache(text.hashCode().toString())
}

internal fun MQuestion.setAnswered() {
    writeBooleanToCache(text.hashCode().toString(), true)
}