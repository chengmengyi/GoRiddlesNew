package game.riddles.core.top

import androidx.databinding.BaseObservable

abstract class BaseViewDataNonComp : BaseObservable() {

    open fun onCleared() {}
}