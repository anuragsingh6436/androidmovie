package com.example.android.dagger.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter: RecyclerView.Adapter<BaseViewHolder<BaseRecyclerItem>>() {

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseRecyclerItem>

    val items = ArrayList<BaseRecyclerItem>()

    fun setItems(items:ArrayList<BaseRecyclerItem>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setItem(item: BaseRecyclerItem, position: Int = 0) {
        this.items.add(item)
        notifyDataSetChanged()
    }
}