package game.riddles.core.adapter.data

import game.riddles.core.adapter.BaseItemData

internal class SimpleListProvider(private val data: List<BaseItemData>) : IRvDataProvider {
    override fun fetchData(callback: (data: List<BaseItemData>) -> Unit) {
        callback(data)
    }
}