package game.riddles.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import game.riddles.core.adapter.data.IRvDataProvider
import game.riddles.core.adapter.data.LiveDataProvider
import game.riddles.core.adapter.data.SimpleListProvider
import game.riddles.core.utils.bindingView

class BaseRvAdapter(private val adapterContext: Context) :
    RecyclerView.Adapter<BaseRvAdapter.Vh>() {

    private val data = mutableListOf<BaseItemData>()
    private val typeMap = hashMapOf<Int, Int>()
    private lateinit var dataProvider: IRvDataProvider

    class Vh(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val layoutResId = typeMap.getOrElse(viewType) { -1 }
        if (layoutResId == -1) throw IllegalStateException("Please call BaseRvAdapter#bingLayout(layoutResId, viewType)")
        return Vh(parent.bindingView(layoutResId))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val data = data[position]
        holder.binding.run {
            lifecycleOwner = (adapterContext as? LifecycleOwner)
            setVariable(1, data)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getItemViewType()
    }

    fun bingLayout(
        layoutResId: Int,
        viewType: Int = BaseItemData.ITEM_VIEW_TYPE_DEF
    ): BaseRvAdapter {
        typeMap[viewType] = layoutResId
        return this
    }

    private fun bindDataProvider(provider: IRvDataProvider) {
        dataProvider = provider
        dataProvider.fetchData {
            updateDataSet(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateDataSet(newData: List<BaseItemData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    @Suppress("unused")
    fun bindSimpleListData(data: List<BaseItemData>) {
        bindDataProvider(SimpleListProvider(data))
    }

    fun bindLiveData(owner: LifecycleOwner, source: LiveData<List<BaseItemData>>) {
        bindDataProvider(LiveDataProvider(owner, source))
    }
}