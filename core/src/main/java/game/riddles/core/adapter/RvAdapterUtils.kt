package game.riddles.core.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setBaseAdapter(): BaseRvAdapter {
    val baseRvAdapter = BaseRvAdapter(context)
    adapter = baseRvAdapter
    return baseRvAdapter
}

fun RecyclerView.grid(row: Int): RecyclerView {
    layoutManager = GridLayoutManager(context, row)
    return this
}