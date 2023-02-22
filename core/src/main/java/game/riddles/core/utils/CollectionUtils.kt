package game.riddles.core.utils

import kotlin.math.min
import kotlin.random.Random

fun <E> List<E>.randomSubList(length: Int): List<E> {
    val randomSize = min(length, size)
    val randomIndexList = mutableSetOf<Int>()
    while (randomIndexList.size < randomSize) {
        randomIndexList.add(Random.nextInt(0, size))
    }
    return randomIndexList.map { get(it) }.toList()
}

fun <T> Iterator<T>.forEachRemain(accept: (e: T) -> Unit) {
    while (hasNext()) {
        accept(next())
    }
}