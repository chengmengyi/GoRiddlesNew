package game.riddles.core.utils

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setEditorActionCallback(
    onSearchAction: (() -> Unit)? = null
) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> onSearchAction?.invoke()
        }
        false
    }
}