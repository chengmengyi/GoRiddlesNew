package game.riddles.core.utils

import android.view.View
import com.blankj.utilcode.util.ClickUtils

fun View.applySingleDenouncingClick(action: (v: View) -> Unit) {
    ClickUtils.applySingleDebouncing(this) { action(it) }
}