package game.riddles.core.adapter

import game.riddles.core.top.BaseViewDataNonComp

abstract class BaseItemData : BaseViewDataNonComp() {
    companion object {
        const val ITEM_VIEW_TYPE_DEF = 800
    }

    open fun getItemViewType(): Int {
        return ITEM_VIEW_TYPE_DEF
    }
}