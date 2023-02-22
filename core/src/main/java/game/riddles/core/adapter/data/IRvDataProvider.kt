package game.riddles.core.adapter.data

import game.riddles.core.adapter.BaseItemData

interface IRvDataProvider {

    fun fetchData(callback: (data: List<BaseItemData>) -> Unit)
}