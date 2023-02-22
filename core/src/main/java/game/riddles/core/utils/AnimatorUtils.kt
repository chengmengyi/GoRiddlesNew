package game.riddles.core.utils

import android.animation.ValueAnimator

fun ValueAnimator.dismiss() {
    removeAllUpdateListeners()
    cancel()
}