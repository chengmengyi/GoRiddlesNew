package game.riddles.core.utils

import androidx.lifecycle.Lifecycle

fun Lifecycle.isOnResume(): Boolean {
    return currentState == Lifecycle.State.RESUMED
}

fun Lifecycle.isOnDestroyed(): Boolean {
    return currentState == Lifecycle.State.DESTROYED
}