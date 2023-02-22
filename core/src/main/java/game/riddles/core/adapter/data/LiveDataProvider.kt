package game.riddles.core.adapter.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import game.riddles.core.adapter.BaseItemData

internal class LiveDataProvider(
    private val owner: LifecycleOwner,
    private val source: LiveData<List<BaseItemData>>
) :
    IRvDataProvider {

    override fun fetchData(callback: (data: List<BaseItemData>) -> Unit) {
        source.observe(owner) {
            callback(it)
        }
    }
}