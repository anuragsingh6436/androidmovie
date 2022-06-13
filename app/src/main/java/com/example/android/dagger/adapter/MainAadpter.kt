package com.example.android.dagger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import com.example.android.dagger.R
import com.example.android.dagger.base.BaseAdapter
import com.example.android.dagger.base.BaseRecyclerItem
import com.example.android.dagger.base.BaseViewHolder
import com.example.android.dagger.databinding.AdapterItemBinding
import com.example.android.dagger.viewModel.MovieItemViewModel
import java.util.*
import kotlin.collections.ArrayList


class MainAadpter : BaseAdapter(),Filterable {


    val initialItemsList = ArrayList<MovieItemViewModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseRecyclerItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding: AdapterItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.adapter_item, parent, false
        )
        return MainViewHolder(dataBinding.root, dataBinding) as BaseViewHolder<BaseRecyclerItem>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseRecyclerItem>, position: Int) {
        holder.onBindData(this.items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MainViewHolder(itemView: View, val dataBinding: AdapterItemBinding) :
        BaseViewHolder<MovieItemViewModel>(itemView) {

        override fun onBindData(item: MovieItemViewModel, position: Int) {
            with(dataBinding) {
                model = item
                executePendingBindings()
            }
        }
    }

    override fun getFilter(): Filter {
        return  exampleFilter
    }

    var exampleFilter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterResults = arrayListOf<MovieItemViewModel>()
            if(charSequence.isEmpty()) {
                filterResults.addAll(initialItemsList)
            } else {
                val filterPattern:String = charSequence.toString().lowercase().trim()
                initialItemsList.forEach {
                    if(it.data.title.lowercase().contains(filterPattern)) {
                        filterResults.add(it)
                    }
                }
            }
            return  FilterResults().also {
                it.values = filterResults
            }
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            items.clear()
            items.addAll(filterResults.values as ArrayList<MovieItemViewModel>)
            notifyDataSetChanged()
        }
    }

}